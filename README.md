# docker-artifactory

Refer to the [JFrog Artifactory Upgrade Guide for Docker](https://www.jfrog.com/confluence/display/RTF/Upgrading+Artifactory#UpgradingArtifactory-DockerInstallation),
the [JFrog Docker Installation Guide](https://www.jfrog.com/confluence/display/RTF/Installing+with+Docker)
and the [JFrog Artifactory Release Notes](https://www.jfrog.com/confluence/display/RTF/Release+Notes).

Setup the host to configure and start Artifactory Pro and associated docker containers. HTTPS is enabled by default. 
Be aware that you may need to add an intermediate cert for it. I had to do this for Red Hat's CND.
 
1. You have to figure out what the intermediate cert is that you need. 
   You can do this by running an openssl command to get the common name of the cert, 
   then searching the web for it. You'll end up at the certificate issuer's webpage as they 
   maintain the list of intermediate certs. If you're being extra careful, verify the checksum's 
   match of the cert you 'think' is the one on their website versus the one referenced by the cert you have. 
   I won't go into how to do that. Download the pem file.
2. This [page](https://www.digicert.com/ssl-support/pem-ssl-creation.htm) told me how to combine 
   the pem files together - just open them in a text editor and just append the contents of the 
   intermediate cert's pem file onto the certicate's pem file. Once combined, you're good to go.

If the system has an Internal CA certificate on the system (placed there by the docker-host role),
it will be automatically applied and used in the Artifactory image.

## Requirements

Run the role docker-host on the host. The following files are required to be present. 
Ensure they're vaulted before committing them to source code control.

* files/artifactory.lic
* files/<docker_artifactory_dns>.key
* files/<docker_artifactory_dns>.pem

## Role Variables

### REQUIRED

* docker_artifactory_dns: The DNS name of the intended Artifactory instance.
* docker_artifactory_artifactory_user_password: The password for the user 'artifactory'. 
  This user has the UID of 1030 and is used inside the docker container.
* docker_artifactory_postgres_user: The user to create and connect to the Postgres instance with. This should be vaulted.
* docker_artifactory_postgres_password: The password for the Postgres user. This should be vaulted.
* docker_artifactory_data: The path on the docker host to store all the data which should be persistent.

### OPTIONAL

* docker_artifactory_version_artifactory_pro: The version of Artifactory Pro to pull from Docker.
* docker_artifactory_version_postgresql: The version of PostgreSQL Pro to pull from Docker.
* docker_artifactory_version_nginx: The version of NGINX to pull from Docker.
* docker_artifactory_certs_to_trust: A list of certificates to add into 
  Artifactory's java truststore and whether they are remote or not.
  Useful if you're using a private CA for Artifactory's web certificates. 
  Also useful if Artifactory needs to interact with other web applications whose
  certificates aren't in the java truststore, like RedHat's CND.

Make sure you get any passwords vaulted so they're not in plain text!

## Dependencies

None

## Example Playbook

Again, make sure you get that password vaulted so it's not in plain text!

    - hosts: servers
      vars:
        docker_artifactory_dns: artifactory.COMPANY.com
        docker_artifactory_postgres_user: puser
        docker_artifactory_postgres_password: ppassword
        docker_artifactory_certs_to_trust:
          # You baked your private CA certificate into the base image, use remote_src yes.
          - { path: '/etc/pki/ca-trust/custom/private_ca.pem', remote_src: yes }
          # The RHEL CND certificate. It's not included in the Java truststore by default.
          # May make more sense to get it from the playbook than to bake it into the base image. Use remote_src no.
          - { path: 'files/rhel_cnd.pem', remote_src: no }
      roles:
         - role: docker-artifactory

## License

BSD

## Author Information

Jeremy Cornett <jeremy.cornett@forcepoint.com>