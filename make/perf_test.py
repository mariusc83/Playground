import datetime
import argparse
import os
import sys
import subprocess
import platform
import threading
import time
import errno


def get_devices(adb_path):
    devices = []
    print "Getting devices..."
    try:
        devices_output = get_adb_devices_output(adb_path)
        for line in devices_output.stdout:
            if len(line) > 0 and not line.lower().strip() == "list":
                devices.append(line.strip())
        if (len(devices) == 0):
            sys.stderr.write("No connected device was found...")
            sys.stderr.close()
            sys.exit(1)

    except OSError:
        sys.stderr.write(OSError.message)
        sys.stderr.close()
        sys.exit(1)

    print "Found devices: " + ''.join(devices)
    return devices


def get_adb_devices_output(adb_path):
    if (platform.system().lower() == "windows"):
        return subprocess.Popen('''for /f "tokens=1" %i in (' ''' + adb_path + ''' devices ') do @echo %i''',
                                stdout=subprocess.PIPE,
                                stderr=subprocess.STDOUT,
                                shell=True)
    else:
        return subprocess.Popen(adb_path + " devices | awk 'NR>1 {print $1}'",
                                stdout=subprocess.PIPE,
                                stderr=subprocess.STDOUT,
                                shell=True)


def wake_up(adb_path, devices):
    for i in range(0, len(devices)):  # Doing this for each device
        print  "Waking up device : " + devices[i] + "... "
        perm_command = [adb_path,
                        '-s', devices[i],
                        'shell',
                        'input', 'keyevent', '82']
        try:
            subprocess.call(perm_command, shell=False)
            sys.stdout.write("")
        except OSError:
            sys.stderr.write(OSError.message)
            return False
    return True


def enable_permission(adb_path, devices, dest_dir, package_name, permission):
    for i in range(0, len(devices)):  # Doing this for each device
        device_dir = dest_dir + "/" + devices[i]
        print ("Starting dump permission grant for device : " + devices[i] + "... ")
        perm_command = [adb_path,
                        '-s', devices[i],
                        'shell',
                        'pm', 'grant', package_name,
                        permission]
        logs_dir = device_dir + '/logs/'
        createDirs(logs_dir)
        log_file_path = logs_dir + 'enable_perm.log'
        with open(log_file_path, 'w+') as log_file:
            try:
                subprocess.call(perm_command,
                                stdout=log_file,
                                stderr=subprocess.STDOUT,
                                shell=False)
                print ("Permission:" + permission + " enabled")
            except OSError:
                sys.stderr.write(OSError.message)


# Create and start threads to run tests and collect tracing information.
def perf_and_analyze_tests(sdk_path, devices, dest_dir, package_name, archive_option):
    run_tests(dest_dir, archive_option)
    analyze_tests(dest_dir, sdk_path, package_name, devices)


def run_tests(dest_dir, archive_option=False):
    apk_dir = os.path.join('app', 'build', 'outputs', 'apk')
    output_parameter = ''
    output_dir = ''
    if archive_option:
        output_parameter = '--output'
        output_dir += 'spoon-output_' + datetime.datetime.now().strftime("%Y-%m-%d")
    spoon_command = ['java', '-jar',
                     './/make//tools//lib//spoon-runner-1.7.1-jar-with-dependencies.jar',
                     '--apk', os.path.join(apk_dir, 'app-debug.apk'),
                     '--test-apk', os.path.join(apk_dir, 'app-debug-androidTest.apk'),
                     '--grant-all',
                     '--fail-if-no-device-connected',
                     output_parameter,
                     output_dir]
    spoon_log_path = os.path.join(dest_dir, 'capture_spoon.log')
    print 'Spoon started'
    with open(spoon_log_path, 'w') as spoon_log:
        try:
            subprocess.call(spoon_command,
                            stdout=spoon_log,
                            stderr=subprocess.STDOUT,
                            shell=False)
        except OSError:
            sys.stderr.write(OSError.message)
    print 'Spoon finished'


def analyze_tests(dest_dir, sdk_path, package_name, devices):
    package_parameter = '--app=' + package_name
    systrace_path = os.path.join(sdk_path, 'platform-tools', 'systrace',
                                 'systrace.py')

    for device_id in devices:
        systrace_command = ['python', systrace_path,
                            '--serial=' + device_id,
                            package_parameter,
                            '--time=20',
                            '-o', os.path.join(dest_dir, 'trace.html'),
                            'gfx', 'input', 'view', 'wm', 'am', 'sm', 'hal',
                            'app', 'res', 'dalvik', 'power', 'freq', 'freq',
                            'idle', 'load']
        print 'Executing systrace for device : ' + device_id
        createDirs(dest_dir)
        systrace_log_path = dest_dir + '/' + 'capture_systrace.log'
        with open(systrace_log_path, 'w') as systrace_log:
            try:
                subprocess.call(systrace_command,
                                stdout=systrace_log,
                                stderr=subprocess.STDOUT,
                                shell=False)
            except OSError:
                sys.stderr.write(OSError.message)
        print 'Stopping systrace execution for device : ' + device_id


def createDirs(path):
    try:
        if not os.path.exists(path):
            os.makedirs(path)
    except OSError as exception:
        if exception != errno.EEXIST:
            raise


def main():
    sdkPath = os.environ.get("ANDROID_HOME")
    # projectPath = os.path.join(os.getcwd(),"..")
    # gradlewPath = os.path.join(projectPath, "gradlew.bat")
    if not os.path.exists(sdkPath):
        print "You should set your ANDROID_HOME as your environment path"
        exit(1)
    parser = argparse.ArgumentParser()
    parser.add_argument("--logs_path", type=str,
                        default='./logs/' + datetime.datetime.now().strftime("%Y-%m-%d"))
    parser.add_argument("--package_name", type=str,
                        default="org.mariusc.gitdemo.test")
    args = parser.parse_args()
    print "Logs path: " + args.logs_path
    print "Sdk path: " + sdkPath

    adb_path = os.path.join(sdkPath, "platform-tools", "adb")
    devices = get_devices(adb_path)
    wake_up(adb_path, devices)
    install_app_command = ['gradlew.bat', ":app:assembleDebug"]
    installLogPath = args.logs_path + "/install_log.log"
    createDirs(args.logs_path)
    with open(installLogPath, "w") as opened_log_path:
        try:
            subprocess.call(install_app_command, stderr=subprocess.STDOUT, stdout=opened_log_path, shell=False)
        except OSError as exception:
            sys.stderr(exception.message)

    enable_permission(adb_path, devices, args.logs_path, args.package_name, 'android.permission.WRITE_EXTERNAL_STORAGE')
    enable_permission(adb_path, devices, args.logs_path, args.package_name, 'android.permission.DUMP')
    test_thread = threading.Thread(name='TestThread', target=perf_and_analyze_tests,
                                   args=(sdkPath, devices, args.logs_path, args.package_name, True))
    trace_time_completion = time.time()

    test_thread.start()
    test_thread.join()

    test_time_completion = int(time.time())
    print ('Time between test and trace thread completion: ' + str(test_time_completion - trace_time_completion) + 's')


if __name__ == '__main__':
    main()
