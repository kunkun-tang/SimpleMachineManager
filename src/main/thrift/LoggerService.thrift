namespace scala core

exception WriteException {}
exception ReadException {}

service LoggerService {
  string log(1: string message, 2: i32 logLevel) throws (1: WriteException writeEx);
  i32 getLogSize() throws (1: ReadException readEx);
}