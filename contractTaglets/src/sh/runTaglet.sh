# /bin/sh

javadoc \
				-tagletpath ../../target/classes \
				-taglet be.peopleware.taglet.contract.Registrar \
				-taglet be.peopleware.taglet.team.Registrar \
				-d ../../docs \
				-classpath  ../../target/classes \
				-sourcepath ../java \
				-subpackages \
				be.peopleware.taglet

