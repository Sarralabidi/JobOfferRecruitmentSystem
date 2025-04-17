import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import Peer from 'peerjs';

@Component({
  selector: 'app-virtual-meeting',
  templateUrl: './virtual-meeting.component.html',
  styleUrls: ['./virtual-meeting.component.css']
})
export class VirtualMeetingComponent implements OnInit {
  myPeerId: string = '';
  eventId: string = '';
  peer: any;
  currentCall: any;

  @ViewChild('localVideo') localVideoRef!: ElementRef<HTMLVideoElement>;
  @ViewChild('remoteVideo') remoteVideoRef!: ElementRef<HTMLVideoElement>;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.eventId = this.route.snapshot.paramMap.get('id') ?? '';
    this.initPeer();
  }

  initPeer(): void {
    this.peer = new Peer(this.eventId); // 🔥 peer ID basé sur l'eventId

    this.peer.on('open', (id: string) => {
      console.log('PeerJS ready with ID:', id);
      this.myPeerId = id;
    });

    this.peer.on('call', (call: any) => {
      navigator.mediaDevices.getUserMedia({ video: true, audio: true })
        .then((stream) => {
          this.localVideoRef.nativeElement.srcObject = stream;
          this.localVideoRef.nativeElement.play();

          call.answer(stream);
          call.on('stream', (remoteStream: MediaStream) => {
            this.remoteVideoRef.nativeElement.srcObject = remoteStream;
            this.remoteVideoRef.nativeElement.play();
          });

          this.currentCall = call;
        })
        .catch(err => {
          console.error('Failed to get local stream', err);
        });
    });
  }

  callPeer(): void {
    navigator.mediaDevices.getUserMedia({ video: true, audio: true })
      .then((stream) => {
        this.localVideoRef.nativeElement.srcObject = stream;
        this.localVideoRef.nativeElement.play();

        const call = this.peer.call(this.eventId, stream); // 🎯 utilise eventId
        call.on('stream', (remoteStream: MediaStream) => {
          this.remoteVideoRef.nativeElement.srcObject = remoteStream;
          this.remoteVideoRef.nativeElement.play();
        });

        this.currentCall = call;
      })
      .catch(err => {
        console.error('Failed to get local stream', err);
      });
  }
}
