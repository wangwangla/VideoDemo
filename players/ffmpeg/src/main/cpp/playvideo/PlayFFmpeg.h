//
// Created by 28188 on 2021/10/18.
//

#ifndef VIDEODEMO_PLAYFFMPEG_H
#define VIDEODEMO_PLAYFFMPEG_H

#include <ctring.h>

class PlayFFmpeg {
public:
    PlayFFmpeg(const char *dataSource);
    ~PlayFFmpeg();

private:
    char *dataSource;
};


#endif //VIDEODEMO_PLAYFFMPEG_H
