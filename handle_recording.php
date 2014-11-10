<?php
// tell the caller that they should listen to their recording
// and play the recording back, using the URL that Twilio posted
header("content-type: text/xml");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
$url = $_REQUEST['RecordingUrl'];
?>
<Response>
<Say>Thanks for the cheer! ... take a listen to what you said.</Say>
<Play><?php echo $_REQUEST['RecordingUrl']; ?></Play>
<Say>Goodbye.</Say>
</Response>