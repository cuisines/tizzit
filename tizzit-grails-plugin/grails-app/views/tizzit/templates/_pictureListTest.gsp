<ul>
	<g:each in="${nodes}" var="picture">
 		<li>
			PictureList Item: ${picture.element("image").element("filename").getText()}
		</li>
	</g:each>
</ul>
