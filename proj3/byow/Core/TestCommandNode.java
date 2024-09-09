package byow.Core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static byow.Core.CommandNode.getCommandList;
import static org.junit.Assert.*;

public class TestCommandNode {

    private String charArrayToString(char[] charArray) {
        StringBuilder charRet = new StringBuilder();
        for (char c: charArray) {
            charRet.append(c);
        }
        return charRet.toString();
    }
    @Test
    public void TestVariousInput1() {
        List<CommandNode> commands;

        commands = getCommandList("N12345Swasd");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 12345);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }
    //"", "n12345sWASD"};
    @Test
    public void TestVariousInput2() {

        List<CommandNode> commands;

        commands = getCommandList("N12345SWASD");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 12345);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }


    @Test
    public void TestVariousInput3() {

        List<CommandNode> commands;

        commands = getCommandList("n12345swasd");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 12345);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }

    @Test
    public void TestVariousInput4() {

        List<CommandNode> commands;

        commands = getCommandList("n12345sWASD");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 12345);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }

    @Test
    public void TestVariousInput5() {

        List<CommandNode> commands;

        commands = getCommandList("N123Q45Swasd");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }

    @Test
    public void TestVariousInput6() {

        List<CommandNode> commands;

        commands = getCommandList("N12345SWALSD");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 12345);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.LOAD);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }

    @Test
    public void TestVariousInput7() {

        List<CommandNode> commands;

        commands = getCommandList("nnnnnnn12345swasd");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 12345);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }

    @Test
    public void TestVariousInput8() {

        List<CommandNode> commands;

        commands = getCommandList("nnnnnn12345swasd");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }
    @Test
    public void TestVariousInput9() {

        List<CommandNode> commands;

        commands = getCommandList("n12345sssssWASD");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 12345);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_WEST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_SOUTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }

    @Test
    public void TestVariousInput10() {

        List<CommandNode> commands;

        commands = getCommandList("N999SDDDWWWDDD");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 999);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }

    @Test
    public void TestVariousInput11() {

        List<CommandNode> commands;

        commands = getCommandList("N999SDDD:Q");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.CREATE_NEW_WORLD);
        assertEquals(cn.rNum, 999);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.QUIT_AND_SAVE_GAME);
    }

    @Test
    public void TestVariousInput12() {

        List<CommandNode> commands;

        commands = getCommandList("LWWWDDD");
        CommandNode cn;

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.LOAD);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_NORTH);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);

        cn = commands.removeFirst();
        assertSame(cn.bc, ByowCommandSet.MOVE_EAST);
    }
}
