<?php
// Include the Twilio PHP library
require 'twilio-php/Services/Twilio.php';
require 'vendor/autoload.php';
//Parse libraries 
use Parse\ParseClient;
use Parse\ParseObject;
use Parse\ParseQuery;
ParseClient::initialize('QXRTROGsVaRn4a3kw4gaFnHGNOsZxXoZ8ULxwZmf', 'BCJuFgG7GVxZfnc2mVbt2dzLz4bP7qAu16xaItXB', 'j9TIxQX3zEHkDPfQszCa6ariYTmZ8JU0RC31BKZK');

// Twilio REST API version
$version = "2010-04-01";
// Set our Account SID and AuthToken
$sid = 'AC6918ac7404fcfbb953976c3fe3a1a432';
$token = '9e027d2cffa89f41b75063e1ae9c03b7';
// A phone number you have previously validated with Twilio
$phonenumber = '2244124770';
// Instantiate a new Twilio Rest Client
$client = new Services_Twilio($sid, $token, $version);

//Parse message URL retrieval	
$query = new ParseQuery("MsgURL");
$query->ascending("createdAt");
$latestObj = $query->first();
$messageURL = $latestObj->get("messageURL");




try {
// Initiate a new outbound call
$call = $client->account->calls->create(
$phonenumber, // The number of the phone initiating the call
'8474019260', // The number of the phone receiving call
$messageURL
//'http://demo.twilio.com/welcome/voice/' // The URL Twilio will request when the call is answered
);


$latestObj->destroy();

echo 'Started call: ' . $call->sid;
} catch (Exception $e) {
echo 'Error: ' . $e->getMessage();
}