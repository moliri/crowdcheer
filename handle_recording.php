<?php
// tell the caller that they should listen to their recording
// and play the recording back, using the URL that Twilio posted
header("content-type: text/xml");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
require 'vendor/autoload.php'; 
use Parse\ParseClient;
ParseClient::initialize('QXRTROGsVaRn4a3kw4gaFnHGNOsZxXoZ8ULxwZmf', 'BCJuFgG7GVxZfnc2mVbt2dzLz4bP7qAu16xaItXB', 'j9TIxQX3zEHkDPfQszCa6ariYTmZ8JU0RC31BKZK');
$url = $_REQUEST['RecordingUrl'];

$msgURL = new ParseObject("MsgURL");
$msgURL->set("messageURL", $url);

try {
  $msgURL->save();
  echo 'New object created with objectId: ' . $msgURL->getObjectId();
} catch (ParseException $ex) { 
  // Execute any logic that should take place if the save fails.
  // error is a ParseException object with an error code and message.
  echo 'Failed to create new object, with error message: ' + $ex->getMessage();
}

?>
<Response>
<Say>Thanks for the cheer! ... take a listen to what you said.</Say>
<Play><?php echo $_REQUEST['RecordingUrl']; ?></Play>
<Say>Goodbye.</Say>
</Response>