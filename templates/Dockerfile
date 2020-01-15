FROM docker.bintray.io/jfrog/artifactory-pro:{{ docker_artifactory_version_jfrog_artifactory_pro }}
{% if docker_artifactory_certs_to_trust is defined %}
# Add in any certificates if present.
COPY certs/* /home/certs/
USER root
{% for item in docker_artifactory_certs_to_trust %}
RUN /java/jdk-11.0.2+9/bin/keytool -import -storepass changeit -noprompt -trustcacerts -alias {{ (item.path | basename | splitext)[0] }} -file /home/certs/{{ item.path | basename }} -keystore /java/jdk-11.0.2+9/lib/security/cacerts
{% endfor %}
USER artifactory
{% endif %}