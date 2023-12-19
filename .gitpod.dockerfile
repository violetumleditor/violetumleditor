FROM gitpod/workspace-full-vnc:latest

SHELL ["/bin/bash", "-c"]
RUN source "/home/gitpod/.sdkman/bin/sdkman-init.sh"  \
    && sdk install java 21.0.1-open < /dev/null