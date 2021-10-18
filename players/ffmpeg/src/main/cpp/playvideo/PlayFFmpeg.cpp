//
// Created by 28188 on 2021/10/18.
//

#include "PlayFFmpeg.h"

PlayFFmpeg::PlayFFmpeg(const char *dataSource) {
//  传递过来的参数有可能会被释放掉  所以会进行一次复制
    this->dataSource = new char[strlen(dataSource)];

}
