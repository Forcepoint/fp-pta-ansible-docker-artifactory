import org.artifactory.repo.RepoPath
import org.artifactory.repo.RepoPathFactory
import org.artifactory.request.Request

download {
    beforeDownloadRequest { Request request, RepoPath path ->
        if (path.getRepoKey().equals("packages.cloud.google.com") && (path.toString().contains(".."))){ // path cannot be null
            log.info("Detected original request path which contains double dots in it: " + path.toString() + ". Trying to fix it...")
            try {
                def tmpPath = path.getPath()
                while (tmpPath.lastIndexOf("../")!=-1){
                    start = tmpPath.substring(0,tmpPath.indexOf("../")-1)
                    end = tmpPath.substring(tmpPath.indexOf("../")+3)
                    start = start.substring(0,start.lastIndexOf("/")+1)
                    tmpPath = start+end
                }
                log.info("The original path: " + path.getPath() + " was modified to: " + tmpPath)
                modifiedRepoPath = RepoPathFactory.create(path.repoKey, tmpPath)
            } catch (Exception e){
                log.warn("Unable to override path: " + e.getMessage())
            }
        }
    }
}