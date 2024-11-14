let localVideo = document.getElementById("local-video")
let remoteVideo = document.getElementById("remote-video")
localVideo.style.opacity = 0
remoteVideo.style.opacity = 0
localVideo.onplaying = () => { localVideo.style.opacity = 1 }
remoteVideo.onplaying = () => { remoteVideo.style.opacity = 1 }
let peer
function init(userId) {
    peer = new Peer(userId, {
        port: 443,
        path: '/'
    })
    peer.on('open', () => {
        Android.onPeerConnected()
    })
    listen()
}
let localStream
function listen() {
    peer.on('call', (call) => {
        navigator.getUserMedia({
            audio: true, 
            video: true
        }, (stream) => {
            localVideo.srcObject = stream
            localStream = stream
            call.answer(stream)
            call.on('stream', (remoteStream) => {
                remoteVideo.srcObject = remoteStream
                remoteVideo.className = "primary-video"
                localVideo.className = "secondary-video"
            })
        })
        
    })
}
function Call(otherUserId) {
    navigator.mediaDevices.getUserMedia({
        audio: true,
        video: {
        facingMode:'user'}
    }, (stream) => {
        localVideo.srcObject = stream
        localStream = stream
        const call = peer.call(otherUserId, stream)
        call.on('stream', (remoteStream) => {
            remoteVideo.srcObject = remoteStream
            remoteVideo.className = "primary-video"
            localVideo.className = "secondary-video"

             // Set the audio output to the speaker (if required)
             setAudioOutputToSpeaker();
            
        })
    })
}
function toggleVideo(b) {
    if (b == "true") {
        localStream.getVideoTracks()[0].enabled = true
    } else {
        localStream.getVideoTracks()[0].enabled = false
    }
} 
function toggleAudio(b) {
    if (b == "true") {
        localStream.getAudioTracks()[0].enabled = true
    } else {
        localStream.getAudioTracks()[0].enabled = false
    }
}



 async function setAudioOutputToSpeaker() {
            try {
                // Get all available audio output devices
                const devices = await navigator.mediaDevices.enumerateDevices();
                const audioOutputs = devices.filter(device => device.kind === 'audiooutput');

                // Look for the main speaker (you can adjust this based on your use case)
                const mainSpeaker = audioOutputs.find(device => device.label.toLowerCase().includes('speaker'));
                if (mainSpeaker) {
                    // Set the audio output to the main speaker using setSinkId
                    const remoteVideo = document.getElementById('remote-video');
                    await remoteVideo.setSinkId(mainSpeaker.deviceId);
                    console.log('Audio output switched to main speaker.');
                } else {
                    console.log('Main speaker not found.');
                }
            } catch (err) {
                console.error('Error switching audio output device:', err);
            }
        }

         async function getAudioOutput() {

                        // Get all available audio output devices
                        const devices = await navigator.mediaDevices.enumerateDevices();
                        const audioOutputs = devices.filter(device => device.kind === 'audiooutput');

                      return audioOutputs;
                }