#/bin/bash
konsole -p tabtitle='Коλ9мбдус' --noclose -e "java -jar ./build/libs/grpc-chat-1.0-SNAPSHOT.jar Коλ9мбдус" &
konsole  -p tabtitle='misterX' --noclose -e "java -jar ./build/libs/grpc-chat-1.0-SNAPSHOT.jar misterX" &
konsole  -p tabtitle='outsider' --noclose -e "java -jar ./build/libs/grpc-chat-1.0-SNAPSHOT.jar outsider" &
konsole  -p tabtitle='dude1' --noclose -e "java -jar ./build/libs/grpc-chat-1.0-SNAPSHOT.jar dude1" &