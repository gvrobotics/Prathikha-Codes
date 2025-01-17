package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

// TEST DURING MEETING

@Autonomous
public class SeniorTeamAuton extends LinearOpMode {

    public DcMotor FR, FL, BR, BL, Slides1, Slides2;
    public Servo Elbow1, Elbow2, SClaw, FClaw, SWrist1, SWrist2;
    public Servo Wrist;
    private IMU imu;
    private Orientation angles;

    // double cpi = 50; (this cpi was too short, around 2.5 inches- keeping as backup tho!)
    double cpi = 60;

    // Target distance to move (in inches)
    private double targetDistance = 1;

    // Motor power
    private double motorPower = 0.5;

    // Estimated robot speed (inches per second)
    private double robotSpeed = 10; // Adjust this value based on your robot's speed

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware map
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

        // Set motor directions
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setDirection(DcMotorSimple.Direction.FORWARD);

        Slides1.setDirection(DcMotor.Direction.REVERSE);
        Slides2.setDirection(DcMotor.Direction.FORWARD);
        SClaw.setDirection(Servo.Direction.FORWARD);
        // set directions for FClaw, SWrist1, SWrist2

        // SWrist1 and SWrist2 must be opposite directions!!
        SWrist1.setDirection(Servo.Direction.FORWARD);
        SWrist2.setDirection(Servo.Direction.REVERSE);

        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Slides1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Slides2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize IMU
        imu = hardwareMap.get(IMU.class, "imu");

        waitForStart();

        //_________________________________________________________________________________________________________

        // RIGHT SIDE
        //claw closed, slides up, wrist position set
        SClaw.setPosition(0.5);
        sleep(500);
        linearup(11.25, 0.5);
        SWrist1.setPosition(0.65);
        SWrist2.setPosition(0.65);
        sleep(500);

        //SPEC 1
        //move back to chambers
        backward(19, 0.4);
        sleep(500);
        //claw opens
        SClaw.setPosition(0.9);
        sleep(500);

        //SPEC 1 AFTERMATH
        // wrist drops, move forward (away from chambers), slides down
        SWrist1.setPosition(0.95);
        SWrist2.setPosition(0.95);
        forward(8, 0.4);
        sleep(500);
        lineardown(11.5, 0.5);
        sleep(200);

        //strafe left, spin 180 so spec claw faces wall, set wrist position
        spinRight(24, 0.4);
        sleep(500);
        strafeRight(22, 0.7);
        strafeLeft(6, 0.7);
        sleep(500);
        //spinRight(40, 0.3); // distance 40, power 0.3 is magical


        SWrist1.setPosition(0.95); //why do we need these lines of code
        SWrist2.setPosition(0.95);

        //move backwards (have spec claw move towards wall)
        backward(22, 0.3);
        sleep(500);

        //GETTING SPEC 2
        //close claw, slides up, change wrist position
        SClaw.setPosition(0.5);
        sleep(200);
        linearup(11.5, 0.5);
        SWrist1.setPosition(0.65);
        SWrist2.setPosition(0.65);
        sleep(1000);

        //move forward (away from wall), strafe, spin 180 so spec claw faces chambers
        forward(7, 0.4); // 10 to 5
        sleep(500);
        strafeLeft(17, 0.7); // was 13 before
        sleep(500);
        spinRight(24.5, 0.4);

        //SPEC 2 SCORING
        //after the spin so spec claw faces chambers, move backwards (towards submersive)
        backward(14, 0.45); // MIGHT CHANGEpass
        sleep(500);

        //open claw to drop spec
        SClaw.setPosition(0.9);
        sleep(500);

        // wrist drops, moves away from chamber a lil
        SWrist1.setPosition(0.95);
        SWrist2.setPosition(0.95);
        forward(5, 0.4);
        lineardown(11.5, 0.5);

        //_________________________________________________________________________________________________________


//        SClaw.setPosition(0.9);



        // THIS CODE HAS NOT BEEN TESTED
//        strafeRight(20, 0.3);
//        strafeRight(5, 0.3);
//        backward(15, 0.3);
//
//        strafeRight(6, 0.3);
//        backward(40, 0.3); // push "sample" into parking
//        forward(40, 0.3);
//        strafeRight(12, 0.3);
//        backward(40, 0.3); // push "sample" into parking
//        forward(40, 0.3);
//        strafeRight(12, 0.3);
//        backward(40, 0.3); // push "sample" into parking
//        strafeLeft(4, 0.3);
//        backward(2, 0.3); // PARK


    }


    //METHODS
    private void forward(double distance, double power) {
        // Get initial heading
        angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialHeading = angles.firstAngle;

        // Start moving forward
        FR.setPower(power);
        FL.setPower(power);
        BR.setPower(power);
        BL.setPower(power);

        // Get the current time in milliseconds
        long startTime = System.currentTimeMillis();

        // Move for estimated time
        while ((System.currentTimeMillis() - startTime) < (distance / robotSpeed) * 1000) {
            // Get current heading
            angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double currentHeading = angles.firstAngle;

            // Calculate heading error
            double headingError = currentHeading - initialHeading;

            // Adjust motor powers based on heading error (optional)
            // double leftPower = power - headingError * kP;
            // double rightPower = power + headingError * kP;

            // Set motor powers (with optional heading correction)
            // BR.setPower(leftPower);
            // BL.setPower(leftPower);
            // FR.setPower(rightPower);
            // FL.setPower(rightPower);

            // For now, keep motor powers constant
            BR.setPower(power);
            BL.setPower(power);
            FR.setPower(power);
            FL.setPower(power);

            // Calculate elapsed time
            double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

            // Estimate distance traveled
            double estimatedDistance = elapsedTime * robotSpeed;

            // Display telemetry
            telemetry.addData("Heading", currentHeading);
            telemetry.addData("Motor Power", power);
            telemetry.addData("Time Elapsed", elapsedTime);
            telemetry.addData("Estimated Distance", estimatedDistance);
            telemetry.addData("Heading Error: ", headingError);
            telemetry.update();

            sleep(10); // Adjust sleep time for telemetry updates
        }

        // Stop motors
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
    }

    private void backward(double distance, double power) {
        // Get initial heading
        angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialHeading = angles.firstAngle;

        // Start moving backward
        FR.setPower(-power);
        FL.setPower(-power);
        BR.setPower(-power);
        BL.setPower(-power);

        // Get the current time in milliseconds
        long startTime = System.currentTimeMillis();

        // Move for estimated time
        while ((System.currentTimeMillis() - startTime) < (distance / robotSpeed) * 1000) {
            // Get current heading
            angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double currentHeading = angles.firstAngle;

            // Calculate heading error
            double headingError = currentHeading - initialHeading;

            // Adjust motor powers based on heading error (optional)
            // double leftPower = power - headingError * kP;
            // double rightPower = power + headingError * kP;

            // Set motor powers (with optional heading correction)
            // BR.setPower(leftPower);
            // BL.setPower(leftPower);
            // FR.setPower(rightPower);
            // FL.setPower(rightPower);

            // For now, keep motor powers constant
            BR.setPower(-power);
            BL.setPower(-power);
            FR.setPower(-power);
            FL.setPower(-power);

            // Calculate elapsed time
            double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

            // Estimate distance traveled
            double estimatedDistance = elapsedTime * robotSpeed;

            // Display telemetry
            telemetry.addData("Heading", currentHeading);
            telemetry.addData("Motor Power", power);
            telemetry.addData("Time Elapsed", elapsedTime);
            telemetry.addData("Estimated Distance", estimatedDistance);
            telemetry.addData("Heading Error: ", headingError);
            telemetry.update();

            sleep(10); // Adjust sleep time for telemetry updates
        }

        // Stop motors
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
    }

    private void spinRight(double distance, double power) {
        // Get initial heading
        angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialHeading = angles.firstAngle;

        // Spin
        FR.setPower(-power);
        FL.setPower(power);
        BR.setPower(-power);
        BL.setPower(power);

        // Get the current time in milliseconds
        long startTime = System.currentTimeMillis();

        // Move for estimated time
        while ((System.currentTimeMillis() - startTime) < (distance / robotSpeed) * 1000) {
            // Get current heading
            angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double currentHeading = angles.firstAngle;

            // Calculate heading error
            double headingError = currentHeading - initialHeading;

            // Adjust motor powers based on heading error (optional)
            // double leftPower = power - headingError * kP;
            // double rightPower = power + headingError * kP;

            // Set motor powers (with optional heading correction)
            // BR.setPower(leftPower);
            // BL.setPower(leftPower);
            // FR.setPower(rightPower);
            // FL.setPower(rightPower);

            // For now, keep motor powers constant
            FR.setPower(-power);
            FL.setPower(power);
            BR.setPower(-power);
            BL.setPower(power);

            // Calculate elapsed time
            double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

            // Estimate distance traveled
            double estimatedDistance = elapsedTime * robotSpeed;

            // Display telemetry
            telemetry.addData("Heading", currentHeading);
            telemetry.addData("Motor Power", power);
            telemetry.addData("Time Elapsed", elapsedTime);
            telemetry.addData("Heading Error: ", headingError);
            telemetry.addData("Estimated Distance", estimatedDistance);
            telemetry.update();

            sleep(10); // Adjust sleep time for telemetry updates
        }

        // Stop motors
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
    }

    private void strafeLeft (double distance,  double power) {

        // Get initial heading
        angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialHeading = angles.firstAngle;

        // sets desired power for motors
        BR.setPower(-power);
        BL.setPower(power);
        FR.setPower(power);
        FL.setPower(-power);

        // Get the current time in milliseconds
        long startTime = System.currentTimeMillis();

        // Move for estimated time
        while ((System.currentTimeMillis() - startTime) < (distance / robotSpeed) * 1000) {
            // Get current heading
            angles = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double currentHeading = angles.firstAngle;

            // Calculate heading error
            double headingError = currentHeading - initialHeading;

            // Adjust motor powers based on heading error (optional)
            // double leftPower = power - headingError * kP;
            // double rightPower = power + headingError * kP;

            // Set motor powers (with optional heading correction)
            // BR.setPower(leftPower);
            // BL.setPower(leftPower);
            // FR.setPower(rightPower);
            // FL.setPower(rightPower);

            // For now, keep motor powers constant
            BR.setPower(-power);
            BL.setPower(power);
            FR.setPower(power);
            FL.setPower(-power);

            // Calculate elapsed time
            double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

            // Estimate distance traveled
            double estimatedDistance = elapsedTime * robotSpeed;

            // Display telemetry
            telemetry.addData("Heading", currentHeading);
            telemetry.addData("Motor Power", power);
            telemetry.addData("Time Elapsed", elapsedTime);
            telemetry.addData("Heading Error: ", headingError);
            telemetry.addData("Estimated Distance", estimatedDistance);
            telemetry.update();

            sleep(10); // Adjust sleep time for telemetry updates
        }

        // stop motors
        BR.setPower(0);
        BL.setPower(0);
        FR.setPower(0);
        FL.setPower(0);
    }

    private void strafeRight (double distance,  double power) {

        // Get initial heading
        angles   = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialHeading = angles.firstAngle;

        // sets desired power for motors
        BR.setPower(power);
        BL.setPower(-power);
        FR.setPower(-power);
        FL.setPower(power);


        // Get the current time in milliseconds
        long startTime = System.currentTimeMillis();

        // Move for estimated time
        while ((System.currentTimeMillis() - startTime) < (distance / robotSpeed) * 1000) {
            // Get current heading
            angles = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double currentHeading = angles.firstAngle;

            // Calculate heading error
            double headingError = currentHeading - initialHeading;

            // Adjust motor powers based on heading error (optional)
            // double leftPower = power - headingError * kP;
            // double rightPower = power + headingError * kP;

            // Set motor powers (with optional heading correction)
            // BR.setPower(leftPower);
            // BL.setPower(leftPower);
            // FR.setPower(rightPower);
            // FL.setPower(rightPower);
            // For now, keep motor powers constant
            BR.setPower(power);
            BL.setPower(-power);
            FR.setPower(-power);
            FL.setPower(power);

            // Calculate elapsed time
            double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

            // Estimate distance traveled
            double estimatedDistance = elapsedTime * robotSpeed;

            // Display telemetry
            telemetry.addData("Heading", currentHeading);
            telemetry.addData("Motor Power", power);
            telemetry.addData("Time Elapsed", elapsedTime);
            telemetry.addData("Heading Error: ", headingError);
            telemetry.addData("Estimated Distance", estimatedDistance);
            telemetry.update();

            sleep(10); // Adjust sleep time for telemetry updates

        }
        // stop motors
        BR.setPower(0);
        BL.setPower(0);
        FR.setPower(0);
        FL.setPower(0);
    }

    private void linearup(double inch, double power)
    {
        Slides1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slides1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slides2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slides2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slides1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slides2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // ADD DEBOUNCING TO SLIDES??
        // debounce delay 500 ms

        int a = (int) ((cpi*inch) + Slides1.getCurrentPosition());
        int b = (int) ((cpi*inch) + Slides2.getCurrentPosition());
        Slides1.setTargetPosition(a);
        Slides2.setTargetPosition(b);
        Slides1.setPower(power);
        Slides2.setPower(power);

        if (Slides1.getCurrentPosition() == a && Slides2.getCurrentPosition() == b)
        {
            Slides1.setPower(0);
            Slides2.setPower(0);
        }

        while (Slides2.isBusy() && Slides1.isBusy())
        {
            telemetry.addLine("Linear up");
            telemetry.addData("Target Slides1", "%7d", a);
            telemetry.addData("Target Slides2", "%7d", b);
            telemetry.addData("Actual Slides1", "%7d", Slides1.getCurrentPosition());
            telemetry.addData("Actual Slides2", "%7d", Slides2.getCurrentPosition());
            telemetry.update();
        }

    }

    private void lineardown(double inch, double power)
    {
        Slides1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        Slides1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slides2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        Slides2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
        Slides1.setPower(0);
        Slides2.setPower(0);
    }

}


// FIX LEFT AND RIGHT

//    private void movementRL(AutonTest.robotMotion action, double degree, double power) {
//
//        if(action == AutonTest.robotMotion.left) {
//            // left is moving backwards, right is moving forwards
//            BR.setTargetPosition((int) (BR.getCurrentPosition() + (degree * cpd)));
//            BL.setTargetPosition((int) (BL.getCurrentPosition() - (degree * cpd)));
//            FR.setTargetPosition((int) (FR.getCurrentPosition() + (degree * cpd)));
//            FL.setTargetPosition((int) (FL.getCurrentPosition() - (degree * cpd)));
//
//            // sets desired power for motors
//            BR.setPower(power);
//            BL.setPower(-power);
//            FR.setPower(power);
//            FL.setPower(-power);
//
//            // make motors run to position
//            // done in init
//
//            // loop to get telemetry while motors are running
//            while (BR.isBusy() && BL.isBusy() && FR.isBusy() && FL.isBusy()) {
//                telemetry.addLine("Turning Left");
//
//                telemetry.addData("BR Target", BR.getTargetPosition());
//                telemetry.addData("BL Target", BL.getTargetPosition());
//                telemetry.addData("FR Target", FR.getTargetPosition());
//                telemetry.addData("FL Target", FL.getTargetPosition());
//
//                telemetry.addData("BR Current", BR.getCurrentPosition());
//                telemetry.addData("BL Current", BL.getCurrentPosition());
//                telemetry.addData("FR Current", FR.getCurrentPosition());
//                telemetry.addData("FL Current", FL.getCurrentPosition());
//
//                telemetry.update();
//            }
//            // stop motors
//            BR.setPower(0);
//            BL.setPower(0);
//            FR.setPower(0);
//            FL.setPower(0);
//        }
//
//        if(action == AutonTest.robotMotion.right) {
//            // right is moving backwards, left is moving forwards
//            BR.setTargetPosition((int) (BR.getCurrentPosition() - (degree * cpd)));
//            BL.setTargetPosition((int) (BL.getCurrentPosition() + (degree * cpd)));
//            FR.setTargetPosition((int) (FR.getCurrentPosition() - (degree * cpd)));
//            FL.setTargetPosition((int) (FL.getCurrentPosition() + (degree * cpd)));
//
//            // sets desired power for motors
//            BR.setPower(-power);
//            BL.setPower(power);
//            FR.setPower(-power);
//            FL.setPower(power);
//
//            // make motors run to position
//            // done in init
//
//            // loop to get telemetry while motors are running
//            while (BR.isBusy() && BL.isBusy() && FR.isBusy() && FL.isBusy()) {
//                telemetry.addLine("Turning Right");
//
//                telemetry.addData("BR Target", BR.getTargetPosition());
//                telemetry.addData("BL Target", BL.getTargetPosition());
//                telemetry.addData("FR Target", FR.getTargetPosition());
//                telemetry.addData("FL Target", FL.getTargetPosition());
//
//                telemetry.addData("BR Current", BR.getCurrentPosition());
//                telemetry.addData("BL Current", BL.getCurrentPosition());
//                telemetry.addData("FR Current", FR.getCurrentPosition());
//                telemetry.addData("FL Current", FL.getCurrentPosition());
//
//                telemetry.update();
//            }
//            // stop motors
//            BR.setPower(0);
//            BL.setPower(0);
//            FR.setPower(0);
//            FL.setPower(0);
//        }
//
//    }
