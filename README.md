# docker-artifactory

For information about PTA and how to use it with this Ansible role please visit https://github.com/Forcepoint/fp-pta-overview/blob/master/README.md

Refer to the [JFrog Artifactory Upgrade Guide for Docker](https://www.jfrog.com/confluence/display/RTF/Upgrading+Artifactory#UpgradingArtifactory-DockerInstallation),
the [JFrog Docker Installation Guide](https://www.jfrog.com/confluence/display/RTF/Installing+with+Docker)
and the [JFrog Artifactory Release Notes](https://www.jfrog.com/confluence/display/RTF/Release+Notes).

Setup the host to configure and start Artifactory Pro and associated docker containers. HTTPS is enabled by default. 
Be aware that you may need to add an intermediate cert for it. You can now do this with Artifactory's UI.

One additional thing to note about Artifactory's certificate is how you intend to use docker.
You should consult Artifactory's documentation about hosting docker repositories. 
https://www.jfrog.com/confluence/display/JFROG/HTTP+Settings#HTTPSettings-UsingaReverseProxy
The recommended method is to use subdomains, which I recommend from an ease of use perspective. 
This unfortunately requires a
change in how you craft your CSR to wildcard the subdomain and also how much such a wildcard
certificate costs from a CA. Of note, if you're using the process outlined here 
https://github.com/Forcepoint/fp-pta-host-ptacontroller-master/blob/master/files/README.md you need
to make two modifications.

1. Alter san.cnf to change the CN to have a *. infront of the dns name.

        CN=*.artifactory.company.com

1. Alter san.cnf to have a second DNS entry under alt_names.

        [alt_names]
        DNS.1   = *.artifactory.company.com
        DNS.2   = artifactory.company.com

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

* docker_artifactory_artifactory_pro_tag: The tag of the Artifactory Pro container to pull.
* docker_artifactory_postgresql_tag: The tag of the PostgreSQL container to pull.
* docker_artifactory_postgresql_migrate: When yes, the database is extracted, a new container 
  spun up in its place, and the data reimported. This is run by default when the postgresql 
  tag is changed. You shouldn't need to set this unless you must force the replacement of 
  the postgres data. Afterwards, you should set this back to no.
* docker_artifactory_nginx_tag: The tag of the NGINX container to pull.

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
      roles:
         - role: docker-artifactory

## License

BSD-3-Clause

## Author Information

Jeremy Cornett <jeremy.cornett@forcepoint.com>
