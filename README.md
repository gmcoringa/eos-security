# EOS-Security

## Pre-requires
  buildr
  jruby or ruby 1.8+

## Building
  buildr clean install
  copy the war found in web/target to jetty or tomcat
  start with ??

## Sample of configuration file:
  TODO

## API Documentation
All documentation can be found [here](http://docs.eossecurity.apiary.io/ "EOS Security API Documentation") 

## TODO
  Cache required services
  Implement security on all services (Tenant DONE)
  RDMS Implementation:
  	- Use C3PO for connection pool
  	- Use flyway to generate ddls and sqls
  	- Finish auto-start without any configuration needed (add derby or hsqldb)
  Graph Implementaion:
	- TODO
  Try to replace all List to Set interfaces where it make sense
  Validate documentation VS Rests services
  Resolve all TODO's
  Validate exception throws


## Internal Resources 
{
	"name" : "Tenant",
	"description" : "Resources for Tenant",
	"permissions" : [
		{
			"permission" : "Tenant.Create",
			"description" : "Permission for tenant creation"
		},
		{
			"permission" : "Tenant.Update",
			"description" : "Permission for tenant information update"
		},
		{
			"permission" : "Tenant.Update.State",
			"description" : "Permission for tenant state update"
		},
		{
			"permission" : "Tenant.Delete",
			"description" : "Permission for tenant deletion (purge)"
		},
		{
			"permission" : "Tenant.Update.Data",
			"description" : "Permission for tenant data update"
		},
		{
			"permission" : "Tenant.View.Data",
			"description" : "Permission for tenant data visualization"
		}
	]
},
