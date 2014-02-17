package mrcpsp.process.mode

import mrcpsp.model.main.Mode

/**
 * Created by mateus on 2/16/14.
 */
class ModeOperations {

    /**
     * remove the mode, using as base the index of the clonedMode
     * @param modes
     * @param index
     * @return
     */
    public boolean removeModeFromListByIndex(List<Mode> modes, Integer index) {
        return modes.removeAll{ it.id == index}
    }
}
