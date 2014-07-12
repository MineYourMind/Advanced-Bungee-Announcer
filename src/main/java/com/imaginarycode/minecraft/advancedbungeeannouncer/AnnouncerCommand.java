/**
 * Copyright © 2013 tuxed <write@imaginarycode.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.imaginarycode.minecraft.advancedbungeeannouncer;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnnouncerCommand extends Command {
    public AnnouncerCommand() {
        super("announcer");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length < 1) {
            about(commandSender);
            return;
        }
        switch (strings[0]) {
            case "about":
                about(commandSender);
                break;
            case "reload":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    AdvancedBungeeAnnouncer.getPlugin().reloadConfiguration();
                    commandSender.sendMessage(ChatColor.GREEN + "The plugin was reloaded successfully.");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to reload the plugin.");
                }
                break;
            case "create":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    if (strings.length < 4) {
                        commandSender.sendMessage(ChatColor.RED + "Not enough arguments specified.");
                        commandSender.sendMessage(ChatColor.RED + "/announcer create <id> <server(s)> <line 1>");
                        commandSender.sendMessage(ChatColor.RED + "<server(s)> may be 'global' if the message is going to be sent to all servers.");
                        commandSender.sendMessage(ChatColor.RED + "<server(s)> may have semicolons to separate server names, like hub;pvp.");
                        return;
                    }
                    if (AdvancedBungeeAnnouncer.getAnnouncements().containsKey(strings[1])) {
                        commandSender.sendMessage(ChatColor.RED + "An announcement with this ID already exists.");
                        return;
                    }
                    String message = Joiner.on(" ").join(Arrays.copyOfRange(strings, 3, strings.length));
                    List<String> servers;
                    if (strings[2].contains(";")) {
                        servers = ImmutableList.copyOf(Splitter.on(";").omitEmptyStrings().split(strings[2]));
                    } else {
                        servers = Collections.singletonList(strings[2]);
                    }
                    AdvancedBungeeAnnouncer.getPlugin().addAnnouncement(strings[1], Announcement.create(message, servers));
                    commandSender.sendMessage(ChatColor.GREEN + "New announcement added.");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to create announcements.");
                }
                break;
            case "remove":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    if (strings.length < 2) {
                        commandSender.sendMessage(ChatColor.RED + "Not enough arguments specified.");
                        commandSender.sendMessage(ChatColor.RED + "/announcer remove <id>");
                        return;
                    }
                    if (!AdvancedBungeeAnnouncer.getAnnouncements().containsKey(strings[1])) {
                        commandSender.sendMessage(ChatColor.RED + "An announcement with this ID does not exist.");
                        return;
                    }
                    AdvancedBungeeAnnouncer.getPlugin().removeAnnouncement(strings[1]);
                    commandSender.sendMessage(ChatColor.GREEN + "Announcement removed.");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to remove announcements.");
                }
                break;
            case "setline":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    if (strings.length < 4) {
                        commandSender.sendMessage(ChatColor.RED + "Not enough arguments specified.");
                        commandSender.sendMessage(ChatColor.RED + "/announcer setline <id> <number> <text>");
                        return;
                    }
                    if (!AdvancedBungeeAnnouncer.getAnnouncements().containsKey(strings[1])) {
                        commandSender.sendMessage(ChatColor.RED + "An announcement with this ID does not exist.");
                        return;
                    }
                    Announcement a = AdvancedBungeeAnnouncer.getAnnouncements().get(strings[1]);
                    try {
                        a.getText().set(Integer.valueOf(strings[2]), Joiner.on(" ").join(Arrays.copyOfRange(strings, 3, strings.length)));
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        commandSender.sendMessage(ChatColor.RED + "That line number is invalid.");
                        return;
                    }
                    commandSender.sendMessage(ChatColor.GREEN + "Announcement updated!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to update announcements.");
                }
                break;
            case "addline":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    if (strings.length < 3) {
                        commandSender.sendMessage(ChatColor.RED + "Not enough arguments specified.");
                        commandSender.sendMessage(ChatColor.RED + "/announcer addline <id> <text>");
                        return;
                    }
                    if (!AdvancedBungeeAnnouncer.getAnnouncements().containsKey(strings[1])) {
                        commandSender.sendMessage(ChatColor.RED + "An announcement with this ID does not exist.");
                        return;
                    }
                    Announcement a = AdvancedBungeeAnnouncer.getAnnouncements().get(strings[1]);
                    a.getText().add(Joiner.on(" ").join(Arrays.copyOfRange(strings, 2, strings.length)));
                    commandSender.sendMessage(ChatColor.GREEN + "Announcement updated!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to update announcements.");
                }
                break;
            case "removeline":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    if (strings.length < 3) {
                        commandSender.sendMessage(ChatColor.RED + "Not enough arguments specified.");
                        commandSender.sendMessage(ChatColor.RED + "/announcer removeline <id> <number>");
                        return;
                    }
                    if (!AdvancedBungeeAnnouncer.getAnnouncements().containsKey(strings[1])) {
                        commandSender.sendMessage(ChatColor.RED + "An announcement with this ID does not exist.");
                        return;
                    }
                    Announcement a = AdvancedBungeeAnnouncer.getAnnouncements().get(strings[1]);
                    try {
                        a.getText().remove((int)Integer.valueOf(strings[2]));
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        commandSender.sendMessage(ChatColor.RED + "That line number is invalid.");
                        return;
                    }
                    commandSender.sendMessage(ChatColor.GREEN + "Announcement updated!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to update announcements.");
                }
                break;
            case "list":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    commandSender.sendMessage(ChatColor.YELLOW + "Announcements: " + Joiner.on(", ").join(
                            AdvancedBungeeAnnouncer.getAnnouncements().keySet()));
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to list announcements.");
                }
                break;
            case "info":
                if (commandSender.hasPermission("advancedbungeeannouncer.admin")) {
                    if (strings.length < 2) {
                        commandSender.sendMessage(ChatColor.RED + "Not enough arguments specified.");
                        commandSender.sendMessage(ChatColor.RED + "/announcer info <id>");
                    }
                    Announcement a = AdvancedBungeeAnnouncer.getAnnouncements().get(strings[1]);
                    commandSender.sendMessage("-------------------------------------");
                    commandSender.sendMessage(ChatColor.AQUA + "Announcement ID: " + strings[1]);
                    commandSender.sendMessage(ChatColor.AQUA + "Announcement Text: (lines: " + a.getText().size() + ")");
                    for (String line : a.getText()) {
                        commandSender.sendMessage("- " + ChatColor.translateAlternateColorCodes('&', line));
                    }
                    commandSender.sendMessage(ChatColor.AQUA + "Sent To: " + Joiner.on(", ").join(a.getServers()));
                    commandSender.sendMessage("-------------------------------------");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to get information on announcements.");
                }
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "/announcer <about|reload|create|remove|{set,add,remove}line|list|info>");
        }
    }

    private void about(CommandSender cs) {
        cs.sendMessage(ChatColor.GOLD + "AdvancedBungeeAnnouncer " + AdvancedBungeeAnnouncer.getPlugin().getDescription().getVersion() + " by tuxed");
        cs.sendMessage(ChatColor.GOLD + "This plugin is freely redistributable under the terms of the WTFPL, see http://www.wtfpl.net for more details.");
    }
}
