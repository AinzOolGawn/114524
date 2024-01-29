#pragma once

#include <map>

namespace SqliteMutexHook {
    typedef struct {
        pthread_mutex_t mutex;
    } sqlite3_mutex;

    typedef struct {
        void* pVfs;
        void* pVdbe;
        void* pDfltColl;
        sqlite3_mutex* mutex;
    } sqlite3;

    static std::map<std::string, sqlite3_mutex *> mutex_map = {};
    static int (*sqlite3_open_original)(const char *, sqlite3 **, unsigned int, const char *) = nullptr;

    int sqlite3_open_hook(const char *filename, sqlite3 **ppDb, unsigned int flags, const char *zVfs) {
        auto result = sqlite3_open_original(filename, ppDb, flags, zVfs);
        if (result == 0) {
            auto mutex = (*ppDb)->mutex;
            if (mutex == nullptr) return result;

            auto last_slash = strrchr(filename, '/') + 1;
            if (last_slash > filename) {
                LOGD("sqlite3_open_hook: %s", last_slash);
                mutex_map[last_slash] = mutex;
            }
        }
        return result;
    }

    void init() {
        auto open_database_sig = util::find_signature(
                common::client_module.base, common::client_module.size,
                ARM64 ? "FF FF 00 A9 3F 00 00 F9" : "8D B0 06 46 E7 48",
                ARM64 ? -0x3C : -0x7
        );
        if (open_database_sig == 0) {
            LOGE("sqlite3 openDatabase sig not found");
            return;
        }
        DobbyHook((void *) open_database_sig, (void *) sqlite3_open_hook, (void **) &sqlite3_open_original);
    }
}