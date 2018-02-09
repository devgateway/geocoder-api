
java -server -Xms1G -Xmx2G -XX:MaxMetaspaceSize=256m -XX:+UseConcMarkSweepGC -XX:ReservedCodeCacheSize=128m -DJava.awt.headless=true -XX:+CMSParallelRemarkEnabled -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark -Xverify:none -noverify
-jar geocoder-1.0.jar --spring.config.name=production
