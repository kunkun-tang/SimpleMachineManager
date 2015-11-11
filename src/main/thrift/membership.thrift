namespace scala core

exception MemberShipException {
  1: string description
}

service MemberShip {
  void join(1: string key) throws(1: MemberShipException ex)
  void leave(1: string key) throws(1: MemberShipException ex)
  string ping(1: string key) throws(1: MemberShipException ex)
}
