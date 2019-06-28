package pro.gravit.launchermodules.sashoksupport.socket.legacy.update;

import pro.gravit.launcher.serialize.HInput;
import pro.gravit.launcher.serialize.HOutput;
import pro.gravit.launchermodules.sashoksupport.socket.legacy.Response;
import pro.gravit.launchserver.LaunchServer;
import pro.gravit.launchserver.binary.LauncherBinary;
import pro.gravit.launchserver.socket.Client;
import pro.gravit.utils.helper.SecurityHelper;

import java.io.IOException;

public final class LegacyLauncherResponse extends Response {

    public LegacyLauncherResponse(LaunchServer server, long session, HInput input, HOutput output) {
        super(server, session, input, output);
    }

    @Override
    public void reply() throws IOException {
        // Resolve launcher binary
        LauncherBinary bytes = (input.readBoolean() ? server.launcherEXEBinary : server.launcherBinary);
        if (bytes == null) {
            requestError("Missing launcher binary");
            return;
        }
        writeNoError(output);

        // Update launcher binary
        output.writeByteArray(bytes.getSign(), -SecurityHelper.RSA_KEY_LENGTH);
        output.flush();
        if (input.readBoolean()) {
            output.writeByteArray(bytes.getBytes().getBytes(), 0);
            return; // Launcher will be restarted
        }
        requestError("You must update");
    }
}
