#!/usr/bin/env groovy

def call(String snow_url, String username, String password, String sys_id, String log_file_path) {
  sh """curl -k -u "${username}":"${password}" -X POST '${snow_url}' --form 'table_name="incident"' --form 'table_sys_id="${sys_id}"' --form 'file=@"${log_file_path}"' """
}
