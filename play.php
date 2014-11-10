<?php

//Parse libraries 
require 'vendor/autoload.php';
use Parse\ParseClient;
use Parse\ParseObject;
use Parse\ParseQuery;

ParseClient::initialize('QXRTROGsVaRn4a3kw4gaFnHGNOsZxXoZ8ULxwZmf', 'BCJuFgG7GVxZfnc2mVbt2dzLz4bP7qAu16xaItXB', 'j9TIxQX3zEHkDPfQszCa6ariYTmZ8JU0RC31BKZK');

//Parse message URL retrieval	
$query = new ParseQuery("MsgURL");
$query->ascending("createdAt");
$latestObj = $query->first();
$messageURL = $latestObj->get("messageURL");


	

?>

<Response>
<Play>http://api.twilio.com/2010-04-01/Accounts/AC6918ac7404fcfbb953976c3fe3a1a432/Recordings/REab44f500088cb54c5ec2d167a19013eb.mp3</Play>
</Response>