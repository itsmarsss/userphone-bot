package com.marsss.utils;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import com.marsss.bot.Donate;
import com.marsss.bot.Invite;
import com.marsss.bot.Ping;
import com.marsss.bot.Uptime;
import com.marsss.entertainments.*;

public class Help {
	public static MessageEmbed help(String name) {
		String DESC = "I don't recognize that command :(";
		name = name.toLowerCase();


		// Bot

		switch(name) {



		case "donate":
			DESC = Donate.getHelp();
			break;
		case "invite":
			DESC = Invite.getHelp();
		case "ping":
			DESC = Ping.getHelp();
		case "Uptime":
			DESC = Uptime.getHelp();



		}



		// Entertainments

		switch(name) {
		
		
		
		case "clap":
			DESC = Clap.getHelp();
			break;
		case "color":
			DESC = Colour.getHelp();
			break;
		case "echo":
			DESC = Echo.getHelp();
			break;
		case "eightball":
			DESC = EightBall.getHelp();
			break;
			
			
			
		}
		
		
		
		// Utils
		
		switch(name) {
		case "botinfo":
			DESC = BotInfo.getHelp();
			break;
		case "channelinfo":
			DESC = ChannelInfo.getHelp();
		case "poll":
			DESC = Polls.getHelp();
			break;
		case "roleinfo":
			DESC = RoleInfo.getHelp();
			break;
		case "serverinfo":
			DESC = ServerInfo.getHelp();
			break;
		case "userinfo":
			DESC = UserInfo.getHelp();
			break;
			
			
			
		}
		
		
		
		// Userphone
		
		switch(name) {
		
		
		
		case "call":
			
		}

		Color COLOR = Colour.randColor();
		EmbedBuilder HelpEmd = new EmbedBuilder()
				.setTitle("Help is here!")
				.setDescription(DESC)
				.setFooter("Hope you found this useful!")
				.setColor(COLOR);

		return HelpEmd.build();
	}

	public static String getHelp() {
		return "`help` - help help help";
	}
}
