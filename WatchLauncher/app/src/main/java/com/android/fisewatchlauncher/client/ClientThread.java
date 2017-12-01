package com.android.fisewatchlauncher.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class ClientThread implements Runnable {
	private Socket socket = null;
	private Handler handler = null;
	BufferedReader br = null;

	public ClientThread(Socket s, Handler handler) throws IOException {
		this.socket = s;
		this.handler = handler;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	@Override
	public void run() {
		try {
			String connet = null;
			while ((connet = br.readLine()) != null) {
				Message message = new Message();
				message.what = 0x123;
				message.obj = connet;
				handler.sendMessage(message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
