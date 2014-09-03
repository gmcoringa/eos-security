# Dependencies and profile functions

# Repositories
repositories.remote << "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases"
repositories.remote << "http://repo1.maven.org/maven2"


## Dependencies

SPRING = group('spring-core', 'spring-orm', 'spring-context', 'spring-tx', 'spring-beans', 'spring-web',
	:under => 'org.springframework', :version => '3.2.4.RELEASE')
SPRING_IOC = group('spring-core', 'spring-context', 'spring-beans', 'spring-web',
	:under => 'org.springframework', :version => '3.2.4.RELEASE')

SCANNOTATION = 'org.scannotation:scannotation:jar:1.0.3'

SLF4J = 'org.slf4j:slf4j-api:jar:1.7.5'
#LOG4J = [transitive('org.apache.logging.log4j:log4j-slf4j-impl:jar:2.0-beta9')]
LOG4J = [transitive('log4j:log4j:jar:1.2.17'), 'org.slf4j:slf4j-log4j12:jar:1.7.5']

# Use the same provided by REST_IMPL
JSON_PROVIDER = ['org.codehaus.jackson:jackson-core-asl:jar:1.9.12', 'org.codehaus.jackson:jackson-mapper-asl:jar:1.9.12']

REST = ['javax.ws.rs:javax.ws.rs-api:jar:2.0', 'javax.servlet:javax.servlet-api:jar:3.1.0']
# Resteasy
RESTEASY = transitive('org.jboss.resteasy:resteasy-servlet-initializer:jar:3.0.4.Final', 'org.jboss.resteasy:resteasy-jackson-provider:jar:3.0.4.Final')
REST_IMPL = RESTEASY

# Apache utils
COMMONS_CODEC='commons-codec:commons-codec:jar:1.9'

# Test dependencies
TEST = ['org.springframework:spring-test:jar:3.2.4.RELEASE', 'org.hsqldb:hsqldb:jar:2.3.1']

# Duplicate entries
EXCLUSIONS = ['javassist:javassist:jar:3.12.1.GA', 'org.jboss.logging:jboss-logging:jar:3.1.0.CR2', 'org.slf4j:slf4j-api:jar:1.5.8', 'org.slf4j:slf4j-simple:jar:1.5.8']

# NEO4J
NEO4J = ['org.neo4j:neo4j:jar:2.1.0-M01']

