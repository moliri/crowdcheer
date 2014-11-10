<?php
header("content-type: text/xml");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
?>
<Response>
	<Say> Record your motivational message now. Press # when you are finished. </Say>
	<Record 
		playBeep="true" 
		maxLength="30" 
		finishOnKey="#" 
		action="handle_recording.php" />
</Response>

