      // Parameters
      var playLength = 10;
      var videoUrl = 'VF9-sEbqDvU';

      // A script to get parameters passed through the url
      function getParameterByName(name) {
          name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
          var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
              results = regex.exec(location.search);
          return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
      }

      var passedVideoID = getParameterByName('videoID');
      if (passedVideoID === "") {
        // A default video feed
        passedVideoID = videoUrl;
      }
  
      var startTime = getParameterByName('start');
      if (startTime === "") {
        startTime = 0;
      }
  

      // Load the YouTube API
      var tag = document.createElement('script');

      tag.src = "https://www.youtube.com/iframe_api";
      var firstScriptTag = document.getElementsByTagName('script')[0];
      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

      // Create the iframe
      var player;
      function onYouTubeIframeAPIReady() {
        player = new YT.Player('player', {
          height: '390',
          width: '640',
          videoId: passedVideoID,
          playerVars: {
            controls: 0,
            start: startTime,
            end: startTime + playLength,
          },
          events: {
            'onReady': onPlayerReady,
            'onStateChange': onPlayerStateChange
          }
        });
      }

      // Called when the player is ready
      function onPlayerReady(event) {
        event.target.playVideo();
      }

      // The API calls this function when the players state changes.
      // The function indicates that when playing a video (state=1),
      // the player should play for six seconds and then stop.
      var done = false;
      function onPlayerStateChange(event) {
        if (event.data == YT.PlayerState.PLAYING && !done) {
          setTimeout(restartVideo, playLength * 1000);
        }
      }
      function restartVideo() {
        player.seekTo(startTime);
      }
      function stopVideo() {
        player.stopVideo();
      }