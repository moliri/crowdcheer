<?php
header("content-type: text/xml");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
?>
<Response>
	<Say> Record your 5 second motivational message now. </Say>
	<Record maxLength="5" action="handle_recording.php" />
</Response>

