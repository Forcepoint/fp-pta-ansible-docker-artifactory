FROM docker.bintray.io/jfrog/artifactory-pro:{{ docker_artifactory_version_jfrog_artifactory_pro }}
# If there's an internal certificate, add it to the docker image.
{{ docker_artifactory_add_cainternal }}
# This certificate is needed for setting up a remote repository for Red Hat CDN.
# https://www.jfrog.com/confluence/display/RTF/Managing+Certificates
# https://www.jfrog.com/confluence/display/RTF/Using+a+Self-Signed+Certificate
ADD entitlement_master.crt /home/entitlement_master.pem
USER root
# If there's an internal certificate, add the internal certificate to the java keystore.
{{ docker_artifactory_cainternal_keystore }}
RUN /java/jdk-11.0.2+9/bin/keytool -import -storepass changeit -noprompt -trustcacerts -alias entitlement_master -file /home/entitlement_master.pem -keystore /java/jdk-11.0.2+9/lib/security/cacerts
USER artifactory