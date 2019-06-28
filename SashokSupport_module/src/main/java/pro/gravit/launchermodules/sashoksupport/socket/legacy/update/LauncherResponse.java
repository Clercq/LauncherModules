package pro.gravit.launchermodules.sashoksupport.socket.legacy.update;

import pro.gravit.launcher.serialize.HInput;
import pro.gravit.launcher.serialize.HOutput;
import pro.gravit.launcher.serialize.SerializeLimits;
import pro.gravit.launcher.serialize.signed.DigestBytesHolder;
import pro.gravit.launchermodules.sashoksupport.socket.legacy.Response;
import pro.gravit.launchserver.LaunchServer;
import pro.gravit.launchserver.socket.Client;

import java.io.IOException;
import java.util.Arrays;

public final class LauncherResponse extends Response {

    public LauncherResponse(LaunchServer server, long session, HInput input, HOutput output) {
        super(server, session, input, output);
    }

    @Override
    public void reply() throws IOException {
        // Resolve launcher binary
        DigestBytesHolder bytes = (input.readBoolean() ? server.launcherEXEBinary : server.launcherBinary).getBytes();
        if (bytes == null) {
            requestError("Missing launcher binary");
            return;
        }
        byte[] digest = input.readByteArray(SerializeLimits.MAX_DIGEST);
        if (!Arrays.equals(bytes.getDigest(), digest)) {
            writeNoError(output);
            output.writeBoolean(true);
            output.writeByteArray(bytes.getBytes(), 0);
            return;
        }
        writeNoError(output);
        output.writeBoolean(false);
    }
}
