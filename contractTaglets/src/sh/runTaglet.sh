# /bin/sh

javadoc \
				-tagletpath ../../target/classes \
				-taglet be.peopleware.taglet.standalone.TodoTaglet \
				-taglet be.peopleware.taglet.standalone.MudoTaglet \
				-taglet be.peopleware.taglet.standalone.QuestionTaglet \
				-taglet be.peopleware.taglet.standalone.IdeaTaglet \
				-taglet be.peopleware.taglet.standalone.ResultTaglet \
				-taglet be.peopleware.taglet.standalone.PreTaglet \
				-taglet be.peopleware.taglet.standalone.PostTaglet \
				-taglet be.peopleware.taglet.standalone.InvarTaglet \
				-taglet be.peopleware.taglet.inline.UnderlineTaglet \
				-d ../../docs \
				-classpath  ../../target/classes \
				-sourcepath ../java \
				-subpackages \
				be.peopleware.taglet

