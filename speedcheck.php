<?php

require 'vendor/autoload.php';
 
use Parse\ParseClient;
use Parse\ParseObject;
use Parse\ParseQuery;


 
ParseClient::initialize('QXRTROGsVaRn4a3kw4gaFnHGNOsZxXoZ8ULxwZmf', 'BCJuFgG7GVxZfnc2mVbt2dzLz4bP7qAu16xaItXB', 'j9TIxQX3zEHkDPfQszCa6ariYTmZ8JU0RC31BKZK');



function checkSpeed($goal, $message){
$justStarted = true;

$query = new ParseQuery("Speed");
$query->descending("createdAt");
$latestObj = $query->first();
$latestSpeed = $latestObj->get("speed");

if($latestSpeed >= $goal){
  $justStarted = false;
}

while($justStarted = false){
  if ($latestSpeed < $goal){
    exec("php listen.php");
  }
}

}
?>

