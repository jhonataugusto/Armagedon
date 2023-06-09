package br.com.anticheat.menu;

import br.com.anticheat.AntiCheat;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.util.check.EnabledUtil;
import br.com.anticheat.gui.Button;
import br.com.anticheat.gui.Gui;
import br.com.anticheat.gui.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

public class ChecksMenu {


    private static String getBooleanValue(boolean value) {
        return value ? "§aEnabled" : "§cDisabled";
    }


    public static void openCheckSelectorMenu(Player opener) {
        int size = 11;
        Gui gui = new Gui(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getGuiName()), 27);
        for (Category category : Category.values()) {
            gui.addButton(new Button(1, size, ItemUtil.makeItem(Material.BOOK, 1, "§b§l" + category.name(), Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7Handle §b" + category.name() + " §7checks",
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    ChecksMenu.openChecksMenu(clicker, category.name());
                }
            });

            size += 2;

        }
        gui.addButton(new Button(1, 26, ItemUtil.makeItem(Material.ARROW, 1, "§b§lReturn", Arrays.asList(
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                "§7Return to main page",
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
        ))) {
            @Override
            public void onClick(Player clicker, ClickType clickType) {
                gui.close(clicker);
                MainMenu.openMenu(clicker);
            }
        });
        gui.open(opener);
        opener.updateInventory();
    }

    public static void openChecksMenu(Player opener, String type) {
        Gui gui = new Gui(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getGuiName()), 9 * 5);
        if(type.equals("COMBAT")) {
            gui.addButton(new Button(1, 0, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("ReachA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lReach A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("ReachA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("ReachA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if(clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("ReachA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("ReachA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 1, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("ReachB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lReach B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("ReachB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("ReachB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT)
                        if (EnabledUtil.checkIfIsEnabled("ReachB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachB.enabled", true);
                            opener.updateInventory();
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("ReachB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ReachB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });


            gui.addButton(new Button(1, 2, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AutoclickerA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAutoclicker A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AutoclickerA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AutoclickerA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT)
                        if (EnabledUtil.checkIfIsEnabled("AutoclickerA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerA.enabled", true);
                            opener.updateInventory();
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AutoclickerA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 3, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AutoclickerB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAutoclicker B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AutoclickerB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AutoclickerB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AutoclickerB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AutoclickerB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 4, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AutoclickerC")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAutoclicker C", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AutoclickerC")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AutoclickerC")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AutoclickerC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerC.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerC.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AutoclickerC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerC.autoban", false);

                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerC.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 5, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AutoclickerD")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAutoclicker D", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AutoclickerD")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AutoclickerD")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AutoclickerD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerD.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerD.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AutoclickerD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerD.autoban", false);

                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerD.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 6, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AutoclickerE")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAutoclicker E", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AutoclickerE")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AutoclickerE")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AutoclickerE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerE.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerE.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AutoclickerE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerE.autoban", false);

                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerE.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 7, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AutoclickerF")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAutoclicker F", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AutoclickerF")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AutoclickerF")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AutoclickerF")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerF.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerF.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AutoclickerF")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerF.autoban", false);

                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AutoclickerF.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });


            gui.addButton(new Button(1, 8, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraA.autoban", false);

                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 9, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 10, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraC")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura C", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraC")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraC")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraC.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraC.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraC.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraC.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 11, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraD")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura D", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraD")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraD")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraD.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraD.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraD.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraD.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 12, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraE")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura E", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraE")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraE")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraE.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraE.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraE.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraE.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 13, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraF")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura F", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraF")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraF")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraF")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraF.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraF.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraF")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraF.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraF.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 14, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraG")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura G", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraG")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraG")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraG")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraG.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraG.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraG")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraG.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraG.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 15, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraH")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura H", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraH")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraH")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraH")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraH.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraH.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraH")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraH.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraH.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 16, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraI")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura I", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraI")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraI")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraI")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraI.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraI.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraI")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraI.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraI.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 17, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("KillauraJ")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lKillaura J", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("KillauraJ")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("KillauraJ")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("KillauraJ")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraJ.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraJ.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("KillauraJ")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraJ.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.KillauraJ.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });


            gui.addButton(new Button(1, 18, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 19, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 20, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimC")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim C", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimC")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimC")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimC.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimC.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimC.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimC.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 21, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimD")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim D", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimD")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimD")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimD.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimD.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimD.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimD.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 22, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimE")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim E", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimE")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimE")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimE.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimE.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimE.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimE.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 23, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimF")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim F", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimF")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimF")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimF")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimF.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimF.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimF")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimF.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimF.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 24, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimG")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim G", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimG")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimG")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimG")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimG.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimG.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimG")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimG.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimG.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 25, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimH")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim H", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimH")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimH")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimH")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimH.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimH.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimH")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimH.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimH.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 26, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimI")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim I", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimI")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimI")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimI")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimI.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimI.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimI")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimI.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimI.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 27, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimJ")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim J", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimJ")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimJ")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimJ")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimJ.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimJ.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimJ")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimJ.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimJ.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 28, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimK")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim K", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimK")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimK")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimK")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimK.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimK.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimK")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimK.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimK.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 29, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimL")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim L", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimL")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimL")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimL")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimL.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimL.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimL")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimL.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimL.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 30, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimM")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim M", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimM")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimM")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimM")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimM.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimM.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimM")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimM.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimM.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 31, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("AimO")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lAim O", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("AimO")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("AimO")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("AimO")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimO.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimO.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("AimO")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimO.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.AimO.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 32, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("HitboxA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lHitbox A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("HitboxA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("HitboxA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("HitboxA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.HitboxA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.HitboxA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("HitboxA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.HitboxA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.HitboxA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 33, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("VelocityA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lVelocity A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("VelocityA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("VelocityA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("VelocityA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("VelocityA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 34, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("VelocityB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lVelocity B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("VelocityB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("VelocityB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("VelocityB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("VelocityB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.VelocityB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "COMBAT");
                }
            });

            gui.addButton(new Button(1, 44, ItemUtil.makeItem(Material.ARROW, 1, "§b§lReturn", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7Return to selector page",
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    openCheckSelectorMenu(clicker);
                }
            });
        }
        if(type.equals("MOVEMENT")) {
            gui.addButton(new Button(1, 0, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("SpeedA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lSpeed A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("SpeedA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("SpeedA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("SpeedA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("SpeedA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 1, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("SpeedB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lSpeed B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("SpeedB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("SpeedB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("SpeedB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("SpeedB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 2, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("SpeedC")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lSpeed C", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("SpeedC")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("SpeedC")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("SpeedC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedC.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedC.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("SpeedC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedC.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.SpeedC.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });


            gui.addButton(new Button(1, 3, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("OmniSprintA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lOmniSprint A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("OmniSprintA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("OmniSprintA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("OmniSprintA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.OmniSprintA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.OmniSprintA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("OmniSprintA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.OmniSprintA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.OmniSprintA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 4, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("MotionA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lMotion A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("MotionA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("MotionA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("MotionA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("MotionA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 5, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("MotionB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lMotion B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("MotionB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("MotionB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("MotionB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("MotionB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 6, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("MotionC")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lMotion C", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("MotionC")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("MotionC")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("MotionC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionC.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionC.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("MotionC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionC.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.MotionC.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 7, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("FlyA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lFly A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("FlyA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("FlyA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("FlyA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("FlyA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 8, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("FlyB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lFly B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("FlyB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("FlyB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("FlyB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("FlyB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FlyB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 9, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("StepA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lStep A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("StepA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("StepA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("StepA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("StepA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 10, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("StepB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lStep B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("StepB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("StepB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("StepB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("StepB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.StepB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 11, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("NofallA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lNofall A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("NofallA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("NofallA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {

                    gui.close(clicker);

                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("NofallA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("NofallA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 12, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("NofallB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lNofall B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("NofallB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("NofallB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("NofallB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("NofallB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NofallB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 13, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("FastLadderA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lFastLadder A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("FastLadderA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("FastLadderA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("FastLadderA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FastLadderA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FastLadderA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("FastLadderA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FastLadderA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.FastLadderA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 14, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("NoSlowA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lNoSlow A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("NoSlowA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("NoSlowA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("NoSlowA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("NoSlowA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 15, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("NoSlowB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lNoSlow B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("NoSlowB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("NoSlowB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("NoSlowB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("NoSlowB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.NoSlowB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 16, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("JesusA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lJesus A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("JesusA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("JesusA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("JesusA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.JesusA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.JesusA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("JesusA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.JesusA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.JesusA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 17, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("PhaseA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lPhase A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("PhaseA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("PhaseA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("PhaseA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PhaseA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PhaseA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("PhaseA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PhaseA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PhaseA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 18, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("BoatFlyA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lBoatFly A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("BoatFlyA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("BoatFlyA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("BoatFlyA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BoatFlyA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BoatFlyA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("BoatFlyA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BoatFlyA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BoatFlyA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 19, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("InventoryA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lInventory A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("InventoryA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("InventoryA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("InventoryA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("InventoryA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 20, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("InventoryB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lInventory B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("InventoryB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("InventoryB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("InventoryB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("InventoryB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.InventoryB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });

            gui.addButton(new Button(1, 21, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("ScaffoldA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lScaffold A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("ScaffoldA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("ScaffoldA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("ScaffoldA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ScaffoldA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ScaffoldA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("ScaffoldA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ScaffoldA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.ScaffoldA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "MOVEMENT");
                }
            });


            gui.addButton(new Button(1, 35, ItemUtil.makeItem(Material.ARROW, 1, "§b§lReturn", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7Return to selector page",
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    openCheckSelectorMenu(clicker);
                }
            });
        }
        if(type.equals("PACKET")) {
            gui.addButton(new Button(1, 0, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("BadPacketsA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lBadPackets A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("BadPacketsA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("BadPacketsA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("BadPacketsA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("BadPacketsA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 1, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("BadPacketsB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lBadPackets B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("BadPacketsB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("BadPacketsB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("BadPacketsB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("BadPacketsB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 2, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("BadPacketsC")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lBadPackets C", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("BadPacketsC")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("BadPacketsC")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("BadPacketsC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsC.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsC.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("BadPacketsC")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsC.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsC.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 3, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("BadPacketsD")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lBadPackets D", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("BadPacketsD")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("BadPacketsD")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("BadPacketsD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsD.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsD.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("BadPacketsD")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsD.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsD.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 4, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("BadPacketsE")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lBadPackets E", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("BadPacketsE")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("BadPacketsE")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("BadPacketsE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsE.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsE.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("BadPacketsE")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsE.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.BadPacketsE.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 5, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("TimerA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lTimer A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("TimerA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("TimerA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("TimerA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("TimerA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 6, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("TimerB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lTimer B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("TimerB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("TimerB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("TimerB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("TimerB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.TimerB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 7, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("PingSpoofA")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lPingSpoof A", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("PingSpoofA")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("PingSpoofA")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("PingSpoofA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofA.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofA.enabled", true);
                            opener.updateInventory();
                        }
                    } else if(clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("PingSpoofA")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofA.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofA.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });


            gui.addButton(new Button(1, 8, ItemUtil.makeItem((EnabledUtil.checkIfIsEnabled("PingSpoofB")) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§b§lPingSpoof B", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7State: §b" + getBooleanValue(EnabledUtil.checkIfIsEnabled("PingSpoofB")),
                    "§7Autoban: §b" + getBooleanValue(EnabledUtil.checkIfIsAutoban("PingSpoofB")),
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    if (clickType == ClickType.LEFT) {
                        if (EnabledUtil.checkIfIsEnabled("PingSpoofB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofB.enabled", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofB.enabled", true);
                            opener.updateInventory();
                        }
                    } else if (clickType == ClickType.RIGHT) {
                        if (EnabledUtil.checkIfIsAutoban("PingSpoofB")) {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofB.autoban", false);
                        } else {
                            AntiCheat.getInstance().getFileManager().getSettings().set("checks.PingSpoofB.autoban", true);
                            opener.updateInventory();
                        }
                    }
                    AntiCheat.getInstance().getFileManager().save();
                    AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                    openChecksMenu(clicker, "PACKET");
                }
            });

            gui.addButton(new Button(1, 35, ItemUtil.makeItem(Material.ARROW, 1, "§b§lReturn", Arrays.asList(
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                    "§7Return to selector page",
                    "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
            ))) {
                @Override
                public void onClick(Player clicker, ClickType clickType) {
                    gui.close(clicker);
                    openCheckSelectorMenu(clicker);
                }
            });
        }

        gui.open(opener);
        opener.updateInventory();
    }
    private String getBooleanValue89(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }
}

