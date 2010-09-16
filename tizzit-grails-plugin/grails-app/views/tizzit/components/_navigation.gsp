<ul class="navigation level${level}">
	<g:each in="${navigation.viewcomponent}" var="viewcomponent">
		<li class="nav-item level${level} ${viewcomponent.@onAxisToRoot=='true'?'active':''} ${viewcomponent.@prev?'':'first'} ${viewcomponent.@next?'':'last'} ${viewcomponent.@hasChild=='true'?'hasChild':''}">
			<a class="level${level} ${viewcomponent.@onAxisToRoot=='true'?'active':''}" href="/${viewcomponent.language.text()}/${viewcomponent.url.text()}.html">
				<span>${viewcomponent.linkName.text()}</span>
			</a>
			<g:if test="${viewcomponent.@hasChild=='true' && viewcomponent.viewcomponent}">
				<g:render template="components/navigation" model="${[navigation: viewcomponent, level:level+1]}" plugin="tizzitWeb"/>
			</g:if>
		</li>
	</g:each>
</ul>
