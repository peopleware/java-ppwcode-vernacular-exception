# /bin/sh

javadoc -source 1.4 \
				-author \
				-tagletpath ../../target/classes \
				-taglet be.peopleware.taglet.contract.Registrar \
				-taglet be.peopleware.taglet.team.Registrar \
				-d ../../docs \
				-classpath  ../../target/classes \
				-sourcepath ../java \
				-subpackages \
				be.peopleware.taglet

