<g:if test="${node.internalLink.@displayType[0] == 'block'}">
	<a class="link internalLink" href="${node.internalLink.@viewid[0]}">${node.internalLink.text()}</a>
</g:if>
<g:else>
	<a class="link internalLink" href="${node.internalLink.@viewid[0]}">${node.internalLink.text()}</a>
	<!-- <div class="internalLink">	Non Block Internal Link!</div> -->
</g:else>