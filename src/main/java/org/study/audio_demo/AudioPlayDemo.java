package org.study.audio_demo;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 分析：做一个播放器
 * 1：需要资源。资源的路径
 * 2：需要播放工具。也就是播放界面
 * 3：通过各种事件控制播放的操作
 * 		a：播放    b：停止    
 * @author hp
 *
 */
public class AudioPlayDemo extends JFrame implements ActionListener,ItemListener{

	private boolean isLoop;//是否循环播放
	String[] resource = new String[]{"F:\\chimes.wav","F:\\start.wav"};
	URL voice1 = getClass().getResource(resource[0]);
	URL voice2 = getClass().getResource(resource[1]);
	
	AudioClip sound1 = Applet.newAudioClip(voice1);   //声音剪辑对象
	AudioClip sound2 = Applet.newAudioClip(voice2);	  //声音剪辑对象
	AudioClip chosen = sound1;
	
	
	JComboBox files = new JComboBox(resource);  //文件选择组合框
	JButton playButton = new JButton("播放");
	JButton loopButton = new JButton("循环播放");
	JButton stopButton = new JButton("停止");
	JLabel status = new JLabel("选择播放文件");
	
	JPanel controlPanel = new JPanel();
	//获得窗口内容窗格
	Container container = getContentPane();
	
	public AudioPlayDemo() {
		super("声音播放程序");//设置窗口标题
		//设置组合框选择项
		files.setSelectedIndex(0);
		files.addItemListener(this);
		//给按钮添加监听事件
		playButton.addActionListener(this);
		loopButton.addActionListener(this);
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		
		//把按钮放入面板
		controlPanel.add(playButton);
		controlPanel.add(stopButton);
		controlPanel.add(loopButton);
		
		container.add(files, BorderLayout.NORTH);//选择框放入顶部
		container.add(controlPanel,BorderLayout.CENTER);
		container.add(status, BorderLayout.SOUTH);
		
		setSize(300, 130);  //设置播放窗口的大小
		setVisible(true);	//窗口可视
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //窗口关闭退出程序
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 按钮事件处理
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(chosen == null) {
			status.setText("未选择文件");
			return;
		}
		
		Object source = e.getSource();
		if(source == playButton) {
			stopButton.setEnabled(true);
			loopButton.setEnabled(true);
			chosen.play();     //剪辑声音对象一次
			status.setText("正在播放");
		}
		//循环播放
		if(source == loopButton) {
			isLoop = true;
			chosen.loop();     //循环播放选择的声音
			stopButton.setEnabled(true);
			loopButton.setEnabled(false);
			status.setText("正在循环播放");
		}
		
		if(source == stopButton) {
			if(isLoop) {
				isLoop = false;   //停止循环播放
				chosen.stop();    //停止播放
				loopButton.setEnabled(true);
			} else {
				chosen.stop();
			}
			stopButton.setEnabled(false);
			status.setText("停止播放");
		}
		
	}
	//文件选择组合框事件处理
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(files.getSelectedIndex() == 0) {
			chosen = sound1;
		} else {
			chosen = sound2;
		}
	}
	
	
	public static void main(String[] args) {
		new AudioPlayDemo();
//		System.out.println(System.getProperty("user.dir"));
	}
}
