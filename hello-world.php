<?php
header("content-type: text/xml");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
?>
<Response>
	<Say> Press 1 to record </Say>
	<Gather numDigits="1" action="leave_a_message.php" method="POST"> 
	</Gather>
</Response>

