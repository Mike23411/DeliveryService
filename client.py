import socket
import fileinput
import sys

def sendMessage(host, port, message, timeout=5):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(timeout)
        s.connect((host, port))
        # print ("> " + message)
        s.send("{0}\n".format(message).encode())

        message = []
        data = s.recv(20).decode()
        data = str(data).split()
        message_size = int(data[0])
        total_received = 0
        while total_received < message_size:
            data = s.recv(1024).decode()
            total_received += len(data)
            message.append(data)
        # Could probably use some string builder here but I don't really care until it's optimization time
        return "".join(message)

def runInput(line, host, port, stopPort):
    lineList = str(line).split()
    stopServer = False
    try:
        if (len(lineList) == 1 and lineList[0] == "stop"):
            sendMessage(host, stopPort, "stop")
            try:
                sendMessage(host, port, "stop", .1)
            except:
                print ("stop acknowledged")
                print ("simulation terminated")
        elif (len(lineList) >= 1):
            print (sendMessage(host, port, line), "\n> ", end='')
    except Exception as e:
        print (e)
        print ("ERROR:cannot_connect_to_server")

def commandLineInput(host,port,stopPort):

    for line in fileinput.input():
        runInput(line,host,port,stopPort)
    return 0

def fileInput(filename,host,stop,stopPort):
    with open(filename, 'r') as file:
        for line in file:
            runInput(line,host,port,stopPort)
    return 0


if __name__ == "__main__":

    print ("Welcome to the Grocery Express Delivery Service!\n> ", end='')

    host ="127.0.0.1"
    port = 5000
    stopPort = 6000

    if len(sys.argv) <= 1:
        commandLineInput(host,port,stopPort)
    else:
        for arg in sys.argv[1:]:
            fileInput(arg,host,port,stopPort)