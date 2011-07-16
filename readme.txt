
Dynamic Publishing 

This project includes the Server and Publisher components of
Dynamic Publishing.  This project requires Rhythmyx 6.0

Installation Checklist

1) Unzip the distribution into a temporary directory. 

2) Shut down the Rhythmyx Server 

3) Run the deployment script
	ant -f deploy.xml
This will register the required extensions and copy the JAR files to the appropriate locations.  


4) Start the Rhythmyx Server

5) Change your templates that need to use this functionality to use the dynamicReorgVelocityAssembler plugins. Select from the 
Assembler dropdown in the General tab of your templates.

6) Add the html parameter preview=y to you preview menu actions.  

Mark up your code in the following ways.

Code can be moved and inserted into specified locations in the rendered page.  This
would commonly be in the global template to mark up location above the html or in the head
but could be anywhere in the generated page.

<!-- insert-codeblock:top -->        e.g.  Mark a location with the name "top"


Only display code in context 1 and above and move to the location in the page marked up as top

<!-- codeblock:top-1 -->
Any code
<!-- end codeblock -->

Only show in context 0 (preview). do not move
<!-- codeblock:-0 -->
Any code
<!-- end codeblock -->

Show in any context but move to top location.
<!-- codeblock:top -->
Any code
<!-- end codeblock -->

