<g:if test="${node.internalLink.@displayType[0] == 'block'}">
	<a href="${node.internalLink.@viewid[0]}">${node.internalLink.text()}</a>
</g:if>
<g:else>
<div class="internalLink">	Non Block Internal Link!</div>
</g:else>