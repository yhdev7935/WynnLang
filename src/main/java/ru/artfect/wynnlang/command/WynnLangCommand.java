package ru.artfect.wynnlang.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ru.artfect.wynnlang.*;
import ru.artfect.wynnlang.translate.ReverseTranslation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WynnLangCommand implements ICommand {
    private UpdateManager updateManger;

    public WynnLangCommand(UpdateManager updateManager) {
        this.updateManger = updateManager;
    }

    @Override
    public String getName() {
        return "WynnLang";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/WynnLang";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 0) {
            switch (args[0]) {
                case "toggle":
                    Reference.modEnabled = !Reference.modEnabled;
                    WynnLang.sendMessage("§r모드가 " + (Reference.modEnabled ? "§a활성화되었습니다!" : "§c비활성화되었습니다!") + "§r");
                    ReverseTranslation.reverse();
                    Config.setBoolean("Options", "Enabled", true, Reference.modEnabled);
                    break;
                case "log":
                    Log.enabled = !Log.enabled;
                    WynnLang.sendMessage("§r채팅 로그 " + (Log.enabled ? "§a활성화" : "§c비활성화") + "§r");
                    Config.setBoolean("Options", "Logging", true, Log.enabled);
                    break;
                case "update":
                    updateManger.update();
                    break;
                case "chat":
                    RuChat.enabled = !RuChat.enabled;
                    if (RuChat.enabled) {
                        Reference.ruChat = new RuChat();
                        Reference.ruChat.start();
                    } else {
                        try {
                            Reference.ruChat.closeSocket();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    WynnLang.sendMessage("§rWynnLang 채팅이 " + (RuChat.enabled ? "§a활성화되었습니다!" : "§c비활성화되었습니다!") + "§r");
                    Config.setBoolean("Chat", "Enabled", true, RuChat.enabled);
                    break;
                case "mute":
                    if (args.length == 2) {
                        RuChat.mutePlayer(args[1]);
                    } else {
                        WynnLang.sendMessage("§c메시지를 차단하고 싶은 유저의 닉네임을 입력해주세요!");
                    }
                    break;
                case "info":
                    String online = Reference.ruChat.isAlive() ? String.valueOf(RuChat.online) : "알 수 없음";
                    String chatConnection = Reference.ruChat.isAlive() ? "채팅 서버와의 연결이 완료되었습니다!" : "채팅 서버와의 연결이 존재하지 않습니다!";
                    String playersOnline = "§r온라인 플레이어들: §6" + online;
                    WynnLang.sendMessage("정보: \n" + chatConnection + "\n" + playersOnline);
                    break;
                default:
                    sendHelpMessage();
                    break;
            }
        } else {
            sendHelpMessage();
        }
    }

    private static void sendHelpMessage() {
        WynnLang.sendMessage("명령어\n - /wl toggle - 토글 끄기/켜기" +
                "\n - /wl log - 채팅 로그 활성화 / 비활성화" +
                "\n - /wl chat - 일반 채팅 모드 활성화 / 비활성화" +
                "\n - /wl mute [닉네임] - 개별 플레이어의 메시지 음소거" +
                "\n - /wl info - 정보 보기" +
                "\n - /ru [메시지] - 러시아쪽으로 메시지 보내기" +
                "\n - /ru - 기본 시계? 사용하기 당신이 수집한 모든 메시지가 러시아 채팅으로 바로 보내집니다.");
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public List<String> getAliases() {
        List l = new ArrayList<String>();
        l.add("wynnlang");
        l.add("wl");
        l.add("WL");
        l.add("Wl");
        return l;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        List l = new ArrayList<String>();
        l.add("toggle");
        l.add("log");
        l.add("mute");
        l.add("chat");
        l.add("info");
        return l;
    }
}