namespace scala core

exception GrepException {
  1: string description
}

service Grep {
  string grep(1: string key) throws(1: GrepException ex)
}
