nodemon -e java --exec "mvn package &&  mv target/survivor-s3-plugins-1.0-SNAPSHOT.jar ~/Server/survivor_s3/plugins/ || exit 0 "
