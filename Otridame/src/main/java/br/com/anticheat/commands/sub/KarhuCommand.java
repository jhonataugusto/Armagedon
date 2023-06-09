package br.com.anticheat.commands.sub;

import br.com.anticheat.AntiCheat;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.commands.ACCommand;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.menu.MainMenu;
import br.com.anticheat.menu.SettingsMenu;
import br.com.anticheat.util.check.EnabledUtil;
import br.com.anticheat.util.command.Command;
import br.com.anticheat.util.command.CommandArgs;
import br.com.anticheat.util.file.HastebinUtil;
import br.com.anticheat.util.file.LogUtil;
import br.com.anticheat.util.update.UpdateCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class KarhuCommand extends ACCommand {

    String name2 = Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.main-command.name"));

    @Command(name = "otridame", permission = "otridame.staff")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        if (command.getLabel().equalsIgnoreCase("otridame") || command.getLabel().equalsIgnoreCase(name2)) {
            if (args.length >= 1) {
                if (args[0].toLowerCase().equalsIgnoreCase("gui")) {
                    if (player.hasPermission("otridame.gui")) {
                        MainMenu.openMenu(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have enough permissions.");
                    }
                } else if (args[0].toLowerCase().equalsIgnoreCase("logs")) {
                    if (args.length == 1) {
                        player.sendMessage(ChatColor.RED + "Use: /logs <player>");
                        return;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target != null) {
                        player.sendMessage("§7§m--------------------------------------");
                        player.sendMessage("§7Violations of §b" + target.getName() + "§f:");
                        player.sendMessage(" ");
                        for (Check check : AntiCheat.getInstance().getPlayerDataManager().getPlayerData(target).getCheckManager().getChecks()) {
                            if(AntiCheat.getInstance().getPlayerDataManager().getPlayerData(target).getCheckVl(check) > 0) {
                                player.sendMessage(" §7*  §b" + check.getRawName() + " §7- " +  "x§b" + check.getVl());
                            }
                        }
                        player.sendMessage("§7§m--------------------------------------");
                    } else {
                        player.sendMessage(ChatColor.RED + "Couldn't find that player.");
                    }

                } else if (args[0].toLowerCase().equalsIgnoreCase("pastelogs")) {
                    if (args.length > 1) {
                        player.sendMessage("§7Searching logs...");

                        if (Bukkit.getOfflinePlayer(args[1]) == null) {
                            player.sendMessage("§cSorry, i couldn't find log file for " + args[1] + ".");
                            return;
                        }

                        String target = Bukkit.getOfflinePlayer(args[1]).getName();
                        StringBuilder end = new StringBuilder("Anticheat logs for player " + target + " pasted with " + AntiCheat.getInstance().getFileManager().getCommand() + " " + AntiCheat.getInstance().getBuild());
                        LogUtil.TextFile logFile = new LogUtil.TextFile("" + target.toLowerCase(), "/logs");
                        Bukkit.getScheduler().runTaskAsynchronously(AntiCheat.getInstance(), () -> {
                            try {
                                logFile.readTextFile();
                                if (logFile.getLines().size() <= 0) {
                                    player.sendMessage("§cSorry, i couldn't find log file for " + args[1] + ".");
                                    return;
                                }
                                for (String s : logFile.getLines()) {
                                    end.append("\n").append(s);
                                }
                                String url = HastebinUtil.uploadPaste(end.toString());
                                if (url == null) {
                                    player.sendMessage("§cCouldn't paste logs, maybe the file is too big?");
                                    return;
                                }
                                player.sendMessage("§aPasted logs to: §7" + url);
                            } catch (Exception ex) {
                                player.sendMessage("§cCouldn't paste logs, maybe the file is too big?");
                            }
                        });
                    } else player.sendMessage("§7Usage: " + "/otridame pastelogs player");

                } else if (args[0].toLowerCase().equalsIgnoreCase("autoban")) {
                    if (player.hasPermission("otridame.autoban")) {
                        if (SettingsMenu.getAutoban()) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("autoban", false);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.autoban.disabled"))));
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("autoban", true);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.autoban.enabled"))));
                        }
                        AntiCheat.getInstance().getFileManager().save();
                        AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have enough permissions.");
                    }

                } else if (args[0].toLowerCase().equalsIgnoreCase("alerts")) {
                    AntiCheat.getInstance().getAlertsManager().toggleAlerts(player);
                    player.sendMessage(AntiCheat.getInstance().getAlertsManager().hasAlertsToggled(player) ? (ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.alerts.enabled")))) : (ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.alerts.disabled")))));

                } else if (args[0].toLowerCase().equalsIgnoreCase("verbose")) {
                    AntiCheat.getInstance().getAlertsManager().toggleDevAlerts(player);
                    player.sendMessage(AntiCheat.getInstance().getAlertsManager().hasDevAlertsToggled(player) ? (ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.verbose.enabled")))) : (ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.verbose.disabled")))));

                } else if (args[0].toLowerCase().equalsIgnoreCase("cancelban")) {
                    if (args.length > 1) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if (EnabledUtil.playersToBeBanned.contains(Bukkit.getPlayer(args[1]))) {
                                try {
                                    EnabledUtil.playersToBeBanned.remove(Bukkit.getPlayer(args[1]));
                                } catch (Exception ignored) {
                                }
                                player.sendMessage(ChatColor.GREEN + "Ban cancelled.");
                            } else {
                                player.sendMessage(ChatColor.RED + "Player is not queued for autoban.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Player is not online");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /cancelban <player>");
                    }

                } else if (args[0].toLowerCase().equalsIgnoreCase("exempt")) {
                    if (args.length > 1) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            PlayerData playerData = AntiCheat.getInstance().getPlayerDataManager().getPlayerData(target);
                            if (playerData.isExempt()) {
                                playerData.setExempt(false);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.exempt.disabled").replaceAll("%player%", args[1])));
                            } else {
                                playerData.setExempt(true);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.exempt.enabled").replaceAll("%player%", args[1])));
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.exempt.mention")));
                    }

                } else if (args[0].toLowerCase().equalsIgnoreCase("reload")) {
                    if (player.hasPermission("otridame.reload")) {
                        AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                        player.sendMessage("§b§lKARHU §7// §aConfiguration reloaded!");
                    }

                } else if (args[0].toLowerCase().equalsIgnoreCase("version")) {
                    player.sendMessage("§bVersion§7:");
                    player.sendMessage("§7┃ §fCurrent: §b" + AntiCheat.getInstance().getBuild());
                    player.sendMessage("§7┃ §fLatest: §b" + UpdateCheck.getNewVersion());

                } else if (args[0].toLowerCase().equalsIgnoreCase("info")) {
                    if (args.length > 1) {

                        Player target = Bukkit.getPlayer(args[1]);

                        if (target == null) {
                            player.sendMessage("§cSorry, i couldn't find that player");
                            return;
                        }

                        PlayerData targetData = AntiCheat.getInstance().getPlayerDataManager().getPlayerData(target);

                        player.sendMessage("§7");
                        player.sendMessage("§7Name: §b" + target.getName());
                        player.sendMessage("§7Version: §b" + targetData.getBrand() + " §7| §b" + targetData.getClientVersion().toString().replaceAll("_", ".").replaceAll("v.", ""));
                        player.sendMessage("§7Ping: §b" + targetData.getTransPing() + " §7| §b" + targetData.getPing());
                        player.sendMessage("§7Sensitivity: §b" + Math.round(targetData.getSensitivity() * 200) + "%");
                        player.sendMessage("§7Session duration: §b" + (System.currentTimeMillis() - targetData.getLastJoinTime()) / 1000 / 60 / 60 + "h" + " " + (System.currentTimeMillis() - targetData.getLastJoinTime()) / 1000 / 60 + "m");
                        player.sendMessage("§7");
                    }

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-1"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-2"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-3"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-4"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-5"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-6"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-7"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-8"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-9"))));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-10"))));
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-1"))));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-2"))));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-3"))));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-4"))));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-5"))));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-6")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-7")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-8")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-9")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getSettings().getString("commands.help.line-10")));
            }
        }
    }
}