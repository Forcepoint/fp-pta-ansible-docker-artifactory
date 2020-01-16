FROM docker.bintray.io/jfrog/artifactory-pro:{{ docker_artifactory_version_jfrog_artifactory_pro }}
{% if docker_artifactory_certs_to_trust is defined %}
# Add in any certificates if present.
COPY certs/* /home/certs/
USER root
# Apparently JAVA_HOME isn't set in the artifactory container. Use this code to set it for use with keytool.
RUN JAVA_HOME=$(java -XshowSettings:properties -version 2>&1  >/dev/null | sed -En "s/java.home = (.*)/\1/p") \{% for item in docker_artifactory_certs_to_trust %}

    && $JAVA_HOME/bin/keytool -import -storepass changeit -noprompt -trustcacerts -alias {{ (item.path | basename | splitext)[0] }} -file /home/certs/{{ item.path | basename }} -keystore $JAVA_HOME/lib/security/cacerts {% if not loop.last %}\{% endif %}
{% endfor %}

USER artifactory
{% endif %}