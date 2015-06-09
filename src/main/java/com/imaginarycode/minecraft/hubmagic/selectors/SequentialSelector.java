/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
package com.imaginarycode.minecraft.hubmagic.selectors;

import com.imaginarycode.minecraft.hubmagic.HubMagic;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SequentialSelector implements ServerSelector {
    private int index = 0;

    private synchronized int wrapIndex() {
        int oldIdx = index;
        if (index++ >= HubMagic.getPlugin().getServers().size()) {
            oldIdx = 0;
            index = 0;
        }
        return oldIdx;
    }

    @Override
    public ServerInfo chooseServer(ProxiedPlayer player) {
        ServerInfo info = null;
        int tries = 0;

        while (info == null || (!HubMagic.getPlugin().getPingManager().consideredAvailable(info, player) && tries < 5)) {
            info = HubMagic.getPlugin().getServers().get(wrapIndex());
            tries++;
        }

        return info;
    }
}
