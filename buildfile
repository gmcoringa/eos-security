# Artefacts definitions
require "artifacts/artifacts"

desc 'Security Component, provides User, Tenantcy, Group, Role and Permissions'
define 'eos-security' do
	# Project setup
	project.version = '0.0.1'
	project.group = 'com.eos'
	compile.options.target = '1.7'
	manifest['Copyright'] = 'Unknown 2013'
	# Add javadoc
	package_with_javadoc

	# Commons
	desc 'Commons - Utitlies'
	define 'common-util' do
		package :jar
	end

	desc 'Commons JPA'
	define 'common-jpa' do
		package :jar
		compile.with PERSISTENCE, project('common-util')
		test.with PERSISTENCE, project('common-util')
	end

	desc 'Security API for internal usage'
	define 'api' do
		compile.with project('common-util')
		package :jar
	end

#	desc 'Security Default Implementation'
#	define 'default-impl' do
#		compile.with PERSISTENCE, SPRING, projects('api', 'common-jpa', 'common-util'), SLF4J, COMMONS_CODEC, JSON_PROVIDER
#		test.with PERSISTENCE, transitive(SPRING), projects('api', 'common-util', 'common-jpa'), SLF4J, TEST, HIBERNATE, LOG4J, transitive(JSON_PROVIDER)
#		package :jar
#	end

	desc 'Security Graph Implementation'
	define 'graph-impl' do
		compile.with transitive(NEO4J), SPRING_IOC, projects('api', 'common-util'), SLF4J, COMMONS_CODEC, JSON_PROVIDER 
		test.with transitive(SPRING_IOC), projects('api', 'common-util'), SLF4J, TEST, LOG4J, transitive(JSON_PROVIDER), transitive(NEO4J)
		package :jar
	end

#	desc 'Security Web API'
#	define 'web' do
#		compile.with project('api'), projects('api', 'common-util'), REST, SPRING, SLF4J, SCANNOTATION
#		package(:war).with :libs => projects('api', 'graph-impl')
#		package(:war).libs += artifacts(REST, REST_IMPL, HIBERNATE, LOG4J, SCANNOTATION, transitive(SPRING))
#		package(:war).libs += project('default-impl').compile.dependencies
#		package(:war).libs -= artifacts(EXCLUSIONS)
#	end
		
end

# Project structure
puts project('eos-security').projects.inspect

