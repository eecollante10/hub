cp -R dist/ /usr/local/share/hub
echo '#!/bin/bash' > hub
echo 'java -jar /usr/local/share/hub/hub.jar $*' >> hub
chmod u+x hub
mv hub /usr/local/bin