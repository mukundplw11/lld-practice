package com.lld;

import java.util.List;


/**
 *
 * Lift movement - Up/Down & Stop at specific floors
 * Users - Request for lift for up/down and select floor in lift
 * Lift - Handles user pickup logic, display current floor &
 */
public class Lift {

    public enum LiftStatus {
        UP,
        DOWN,
        IDLE
    }

    public class ElevatorController {
        private Elevator elevator;

        private Integer minFloor;

        private Integer maxFloor;
    }

    public class ElevatorSystem {

        private Integer totalFloors;

        private List<ElevatorController> elevatorControllers;

        public Integer requestElevator(Integer currentFloor, Integer destinationFloor) {
            return null;
        }

    }

    public class Elevator {
        private Integer id;

        private Integer currentFloor;

        private Integer nextStopFloor;

        private LiftStatus status;

        private Boolean doorOpen;
    }

    public class Panel {

    }

    public class ExternalPanel extends Panel {

    }

    public class InternalPanel extends Panel {

    }
}

