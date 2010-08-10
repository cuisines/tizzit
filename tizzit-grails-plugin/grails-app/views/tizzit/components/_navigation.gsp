<ul class="navigation">
	<g:each in="${navigation.viewcomponent}" var="viewcomponent">
		<li class="navigationItem level${level} ${viewcomponent.@onAxisToRoot=='true'?'onAxisToRoot':''} ${viewcomponent.@prev?'':'firstItem'} ${viewcomponent.@next?'':'lastItem'} ${viewcomponent.@hasChild=='true'?'hasChild':''}">
			<a class="level${level}" href="/${viewcomponent.language.text()}/${viewcomponent.url.text()}.html">${viewcomponent.linkName.text()} </a>
			<g:if test="${viewcomponent.@hasChild=='true'}">
				<g:render template="components/navigation" model="${[navigation: viewcomponent, level:level+1]}" plugin="tizzitWeb"/>
			</g:if>
		</li>
	</g:each>
</ul>
