package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

// ORGANIZE

@TeleOp
public class SeniorTele extends OpMode {
    public DcMotor FR, FL, BR, BL, Slides1, Slides2;

    double zoom = 1.1;
    public Servo Elbow1, Elbow2, SClaw, FClaw, SWrist1, SWrist2;
    public Servo Wrist;
    private double powerRY, powerRX, powerLX, powerLY, robotAngle, PowerMultiplier, lf, rb, rf, lb;

    // double cpi = 50; (this cpi was too short, around 2.5 inches- keeping as backup tho!)
    double cpi = 60;

    double cpd = 3.7;
    int inch = 0;


    @Override
    public void init() {
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        BR = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");

        Slides1 = hardwareMap.get(DcMotor.class, "Slides1");
        Slides2 = hardwareMap.get(DcMotor.class, "Slides2");
        Elbow1 = hardwareMap.get(Servo.class, "Elbow1");
        Elbow2 = hardwareMap.get(Servo.class, "Elbow2");
        Wrist = hardwareMap.get(Servo.class, "Wrist");
        SClaw = hardwareMap.get(Servo.class, "SClaw");
        FClaw = hardwareMap.get(Servo.class, "FClaw");
        SWrist1 = hardwareMap.get(Servo.class, "SWrist1");
        SWrist2 = hardwareMap.get(Servo.class, "SWrist2");


        BR.setDirection(DcMotorSimple.Direction.FORWARD);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        FR.setDirection(DcMotorSimple.Direction.FORWARD);
        // note for FL: wires should be flipped (black-red, red-black)
        FL.setDirection(DcMotorSimple.Direction.REVERSE);

        Slides1.setDirection(DcMotor.Direction.REVERSE);
        Slides2.setDirection(DcMotor.Direction.FORWARD);
        SClaw.setDirection(Servo.Direction.FORWARD);


        // SWrist1 and SWrist2 must be opposite directions!!
        SWrist1.setDirection(Servo.Direction.FORWARD);
        SWrist2.setDirection(Servo.Direction.REVERSE);

        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Slides1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Slides2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);

        Slides1.setPower(0);
        Slides2.setPower(0);
        Wrist.setPosition(0.2);

        // start FClaw open
        FClaw.setPosition(0.9);

        // start SClaw closed
        SClaw.setPosition(0.5);
        Elbow1.setPosition(0.9);
        Elbow2.setPosition(0);


        SWrist1.setPosition(0.95);
        SWrist2.setPosition(0.95);

    }

    @Override
    public void loop() {

        powerLX = gamepad1.left_stick_x;
        powerLY = gamepad1.left_stick_y;
        powerRX = gamepad1.right_stick_x;
        powerRY = gamepad1.right_stick_y;

        robotAngle = Math.atan2(powerLX, powerLY);
        telemetry.addData("Robot angle:", robotAngle);
        telemetry.addData("powerRX: ", gamepad1.right_stick_x);
        telemetry.addData("powerRY: ", gamepad1.right_stick_y);
        telemetry.addData("powerLX: ", gamepad1.left_stick_x);
        telemetry.addData("powerLY: ", gamepad1.left_stick_y);

        telemetry.addData("FR: ", FR.getPower());
        telemetry.addData("FL: ", FL.getPower());
        telemetry.addData("BR: ", BR.getPower());
        telemetry.addData("BL: ", BL.getPower());
        telemetry.update();


        PowerMultiplier = Math.sqrt((Math.pow(powerLX, 2) + Math.pow(powerLY, 2)));

        lf = zoom * (PowerMultiplier * -1 * (Math.sin(robotAngle - (Math.PI / 4)))) - powerRX;
        rb = zoom * (PowerMultiplier * -1 * (Math.sin(robotAngle - (Math.PI / 4)))) + powerRX;
        lb = zoom * (PowerMultiplier * Math.sin(robotAngle + (Math.PI / 4))) - powerRX;
        rf = zoom * (PowerMultiplier * Math.sin(robotAngle + (Math.PI / 4))) + powerRX;

        // drivetrain
        FR.setPower(rf);
        FL.setPower(lf);
        BR.setPower(rb);
        BL.setPower(lb);


        // kill
        if (gamepad2.left_bumper) {
            Slides1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Slides2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Slides1.setPower(0);
            Slides2.setPower(0);
            //riggingup();
        }

        // kill
        if (gamepad1.left_bumper) {
            Slides1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Slides2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Slides1.setPower(0);
            Slides2.setPower(0);
        }

        // slides up
        if (gamepad2.y) {

            linearup(11.5, 0.7);

            // wrist set position
            SWrist1.setPosition(0.65);
            SWrist2.setPosition(0.65);

            // backup code if encoder malfunctions
            // Slides1.setPower(0.5);
            // Slides2.setPower(0.5);

        }

        if(gamepad1.a) {
            SWrist1.setPosition(0.95);
            SWrist2.setPosition(0.95);
        }


        // press down once or else battery disconnects
        // will add debounce
        // slides down
        if (gamepad2.a) {

            // wrist set position
            SWrist1.setPosition(0.95);
            SWrist2.setPosition(0.95);
            lineardown(11.5, 0.7);

            // backup code if encoder malfunctions
            // Slides1.setPower(-0.3);
            // Slides2.setPower(-0.3);
        }


        // pull IN elbow + wrist for intake
        if (gamepad2.dpad_left) {

            Elbow1.setPosition(0.9);
            Elbow2.setPosition(0);
            Wrist.setPosition(0.35);
        }

        // pull OUT elbow + wrist for intake
        if (gamepad2.dpad_right) {
            Elbow1.setPosition(0.6);
            Elbow2.setPosition(0.3);
            Wrist.setPosition(0.67); //!!!!
        }

        // RIGGING YIPEE!!
        if (gamepad2.dpad_up) {
            riggingup(34, 0.4);
        }

        // in case mech presses rig instead of up, this is a failsafe
        if (gamepad2.dpad_down) {
            lineardown(34, 0.4); //fail safe
        }

        // max power down
        if (gamepad1.right_bumper) {
            Slides1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Slides2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            Slides1.setPower(0.55);
//            Slides2.setPower(0.55);
            lineardown(34, 0.8);
            Wrist.setPosition(0.1);
            Elbow1.setPosition(0.9);
            Elbow2.setPosition(0);


        }

        // adjust wrist position
        if (gamepad2.right_bumper) {
            Wrist.setPosition(0.75);
        }

        // close FClaw
        if (gamepad2.right_trigger > 0.5) {
            FClaw.setPosition(0.3);

        }

        // open FClaw
        if (gamepad2.left_trigger > 0.5) {
            FClaw.setPosition(0.9);
        }

        // close SClaw
        if (gamepad2.x) {
            SClaw.setPosition(0.5);
        }

        // open SClaw
        if (gamepad2.b) {
            SClaw.setPosition(0.9);
        }

        telemetry.addData("FR Power: ", FR.getPower());
        telemetry.addData("FL Power: ", FL.getPower());
        telemetry.addData("BR Power: ", BR.getPower());
        telemetry.addData("BL Power: ", BL.getPower());
        telemetry.addData("FR Position: ", FR.getCurrentPosition());
        telemetry.addData("FL Position: ", FL.getCurrentPosition());
        telemetry.addData("BR Position: ", BR.getCurrentPosition());
        telemetry.addData("BL Position: ", BL.getCurrentPosition());

        telemetry.addData("Slides1 Power: ", Slides1.getPower());
        telemetry.addData("Slides2 Power: ", Slides2.getPower());
        telemetry.addData("Slides1 Position: ", Slides1.getCurrentPosition());
        telemetry.addData("Slides2 Position : ", Slides2.getCurrentPosition());

        telemetry.addData("SClaw Current Position: ", SClaw.getPosition());
        telemetry.addData("Wrist: ", Wrist.getPosition());

        telemetry.addData("Elbow1: ", Elbow1.getPosition());
        telemetry.addData("Elbow2: ", Elbow2.getPosition());

        telemetry.addData("FClaw: ", FClaw.getPosition());


        telemetry.update();

    }

    private void linearup(double inch, double power) {
        Slides1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slides1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slides2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slides2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slides1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slides2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // ADD DEBOUNCING TO SLIDES??
        // debounce delay 500 ms

        int a = (int) ((cpi * inch) + Slides1.getCurrentPosition());
        int b = (int) ((cpi * inch) + Slides2.getCurrentPosition());
        Slides1.setTargetPosition(a);
        Slides2.setTargetPosition(b);
        Slides1.setPower(power);
        Slides2.setPower(power);

        if (Slides1.getCurrentPosition() == a && Slides2.getCurrentPosition() == b) {
            Slides1.setPower(0);
            Slides2.setPower(0);
        }

        while (Slides2.isBusy() && Slides1.isBusy()) {
            telemetry.addLine("Linear up");
            telemetry.addData("Target Slides1", "%7d", a);
            telemetry.addData("Target Slides2", "%7d", b);
            telemetry.addData("Actual Slides1", "%7d", Slides1.getCurrentPosition());
            telemetry.addData("Actual Slides2", "%7d", Slides2.getCurrentPosition());
            telemetry.update();
        }

    }

    private void lineardown(double inch, double power) {

        Slides1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slides2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        int a = (int) (Slides1.getCurrentPosition() - (inch * cpi));
        int b = (int) (Slides2.getCurrentPosition() - (inch * cpi));
        Slides1.setTargetPosition(a);
        Slides2.setTargetPosition(b);
        Slides1.setPower(power);
        Slides2.setPower(power);
        Slides1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slides2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (Slides1.isBusy() && Slides2.isBusy()) {
            telemetry.addLine("Linear down");
            telemetry.addData("Target", "%7d", a);
            telemetry.addData("Target", "%7d", b);
            telemetry.addData("Actual", "%7d", Slides1.getCurrentPosition());
            telemetry.addData("Actual", "%7d", Slides2.getCurrentPosition());
            telemetry.update();
        }
        Slides1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slides2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slides1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slides2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slides1.setPower(0);
        Slides2.setPower(0);
    }

    private void riggingup(double inch, double power) {
        Slides1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slides1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slides2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slides2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int a = (int) (Slides1.getCurrentPosition() + (inch * cpi));
        int b = (int) (Slides2.getCurrentPosition() + (inch * cpi));
        Slides1.setTargetPosition(a);
        Slides2.setTargetPosition(b);
        Slides1.setPower(power);
        Slides2.setPower(power);
        Slides1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slides2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (Slides1.isBusy() && Slides2.isBusy()) {
            telemetry.addLine("Linear down");
            telemetry.addData("Target", "%7d", a);
            telemetry.addData("Target", "%7d", b);
            telemetry.addData("Actual", "%7d", Slides1.getCurrentPosition());
            telemetry.addData("Actual", "%7d", Slides2.getCurrentPosition());
            telemetry.update();
        }
        Slides1.setPower(0);
        Slides2.setPower(0);
    }
}